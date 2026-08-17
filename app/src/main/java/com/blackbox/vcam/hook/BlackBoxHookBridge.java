package com.blackbox.vcam.hook;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.blackbox.vcam.camera.VirtualCameraEngine;
import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.util.VCamLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * BlackBoxHookBridge
 *
 * Integration layer between our VirtualCameraEngine and BlackBox's internal
 * IAppPlugin / IBlackBoxCore hook points.
 *
 * BlackBox (4.x) exposes plugin interfaces and dynamic proxy hooks for guest
 * app system service calls. We intercept Camera1 and Camera2 calls at the
 * point where BlackBox already proxies them for its cloned app environment.
 *
 * Hook strategy (no Xposed required — BlackBox is already the hook):
 *   1. BlackBox creates a proxy for android.hardware.Camera
 *      → We further wrap that proxy to inject our video frames.
 *   2. BlackBox proxies CameraManager / CameraDevice
 *      → We intercept createCaptureSession / createCameraDevice.
 *
 * This class is instantiated once per guest process by VCamBlackBoxPlugin
 * and registered via BlackBox's plugin callback mechanism.
 */
public class BlackBoxHookBridge {

    private static final String TAG = "VCam_HookBridge";

    private final Context            mHostContext;
    private final VirtualCameraEngine mEngine;

    // Package name of the currently active BlackBox guest
    private String mActiveGuestPackage;

    // Cache of per-guest configs
    private final Map<String, VCamConfig> mGuestConfigs = new HashMap<>();

    public BlackBoxHookBridge(Context hostContext, VirtualCameraEngine engine) {
        mHostContext = hostContext;
        mEngine      = engine;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Guest lifecycle
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Called by VCamBlackBoxPlugin when BlackBox starts a guest app.
     */
    public void onGuestAppStart(String packageName, int userId) {
        VCamLogger.d(TAG, "Guest started: " + packageName + " uid=" + userId);
        mActiveGuestPackage = packageName;
        VCamConfig cfg = mGuestConfigs.get(packageName);
        if (cfg != null && cfg.isEnabled()) {
            mEngine.applyConfig(cfg);
            VCamLogger.d(TAG, "Virtual camera ACTIVE for " + packageName);
        } else {
            mEngine.applyConfig(null); // ensure engine is off for non-vcam guests
        }
    }

    /**
     * Called by VCamBlackBoxPlugin when BlackBox stops a guest app.
     */
    public void onGuestAppStop(String packageName) {
        VCamLogger.d(TAG, "Guest stopped: " + packageName);
        if (packageName.equals(mActiveGuestPackage)) {
            mEngine.applyConfig(null);
            mActiveGuestPackage = null;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Config management (called from BottomSheetVCamMenu)
    // ──────────────────────────────────────────────────────────────────────────

    public void setConfigForGuest(String packageName, VCamConfig config) {
        mGuestConfigs.put(packageName, config);
        if (packageName.equals(mActiveGuestPackage)) {
            mEngine.applyConfig(config);
        }
        VCamLogger.d(TAG, "Config set for " + packageName + ": enabled=" + config.isEnabled());
    }

    public VCamConfig getConfigForGuest(String packageName) {
        return mGuestConfigs.get(packageName);
    }

    public String getActiveGuestPackage() {
        return mActiveGuestPackage;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Camera1 interception (called from BlackBox's internal Camera proxy hooks)
    //
    //  BlackBox intercepts android.hardware.Camera on behalf of guest apps.
    //  We hook AFTER BlackBox's proxy, giving us the real Camera object
    //  to manipulate its surface/preview callbacks.
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Hook: Camera.setPreviewTexture(SurfaceTexture)
     * BlackBox calls this before delegating to the real camera.
     * We intercept and redirect to our virtual surface.
     */
    public void hookCamera1SetPreviewTexture(Camera camera, SurfaceTexture st) {
        if (!mEngine.isActive()) return;
        VCamLogger.d(TAG, "hookCamera1SetPreviewTexture");
        mEngine.onCamera1SetPreview(camera, st);
    }

    /**
     * Hook: Camera.setPreviewDisplay(SurfaceHolder)
     */
    public void hookCamera1SetPreviewDisplay(Camera camera, SurfaceHolder holder) {
        if (!mEngine.isActive()) return;
        VCamLogger.d(TAG, "hookCamera1SetPreviewDisplay");
        Surface s = holder != null ? holder.getSurface() : null;
        if (s != null) {
            mEngine.onCamera1SetPreviewSurface(camera, s);
        }
    }

    /**
     * Hook: Camera.startPreview()
     */
    public void hookCamera1StartPreview(Camera camera) {
        if (!mEngine.isActive()) return;
        VCamLogger.d(TAG, "hookCamera1StartPreview");
        mEngine.onCamera1StartPreview(camera);
    }

    /**
     * Hook: Camera.PreviewCallback.onPreviewFrame(byte[], Camera)
     * Called for every NV21 frame the guest has registered for.
     * We inject our own synthetic frame bytes.
     */
    public byte[] hookCamera1PreviewFrame(byte[] originalFrame, Camera camera,
                                          int width, int height) {
        if (!mEngine.isActive()) return originalFrame;
        return mEngine.interceptPreviewFrame(originalFrame, width, height);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Camera2 interception
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Hook: CameraDevice.createCaptureSession(SessionConfiguration)  [API 28+]
     * Replaces the real session config with one targeting our virtual surface.
     */
    public SessionConfiguration hookCamera2CreateCaptureSession(
            CameraDevice device, SessionConfiguration original) {
        if (!mEngine.isActive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return original;
        }
        VCamLogger.d(TAG, "hookCamera2CreateCaptureSession — injecting virtual surface");
        return mEngine.buildFakeSessionConfiguration(original, device);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Reflection-based hook registration
    //
    //  When BlackBox doesn't provide explicit plugin callbacks for camera,
    //  we use reflection to wrap its internal proxy objects.
    //  This is a fallback; primary path is via VCamBlackBoxPlugin interface.
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Attempt to wrap BlackBox's internal IBlackBoxCore camera proxy object.
     * Requires BlackBox 4.x internal classes (best-effort).
     */
    public void tryReflectionHook() {
        try {
            // BlackBox 4.x uses "com.lody.virtual.client.hook.proxies.am.ActivityManagerProxy"
            // style pattern for service proxies. We look for camera-related ones.
            Class<?> bbCoreClass = Class.forName("com.lody.virtual.client.VirtualCore");
            Object bbCore = bbCoreClass.getMethod("get").invoke(null);

            // Attempt to get the camera service proxy
            Method getCameraProxy = bbCoreClass.getDeclaredMethod("getCameraProxy");
            getCameraProxy.setAccessible(true);
            Object cameraProxy = getCameraProxy.invoke(bbCore);

            if (cameraProxy != null) {
                wrapCameraProxy(cameraProxy);
                VCamLogger.d(TAG, "Reflection hook on BlackBox camera proxy successful");
            }
        } catch (ClassNotFoundException e) {
            VCamLogger.w(TAG, "BlackBox VirtualCore class not found — using plugin API only");
        } catch (Exception e) {
            VCamLogger.w(TAG, "Reflection hook failed: " + e.getMessage() + " — using plugin API only");
        }
    }

    /**
     * Wrap a BlackBox camera proxy object with our interception layer using
     * Java's dynamic Proxy mechanism.
     */
    @SuppressWarnings("unchecked")
    private void wrapCameraProxy(Object originalProxy) {
        Class<?>[] interfaces = originalProxy.getClass().getInterfaces();
        if (interfaces.length == 0) {
            VCamLogger.w(TAG, "Camera proxy has no interfaces, cannot wrap");
            return;
        }

        Object wrappedProxy = Proxy.newProxyInstance(
                originalProxy.getClass().getClassLoader(),
                interfaces,
                (proxy, method, args) -> {
                    String methodName = method.getName();

                    // Intercept camera open to track the Camera object
                    if ("openCamera".equals(methodName) || "open".equals(methodName)) {
                        Object result = method.invoke(originalProxy, args);
                        VCamLogger.d(TAG, "Camera open intercepted via reflection proxy");
                        return result;
                    }

                    // Let everything else pass through
                    return method.invoke(originalProxy, args);
                }
        );

        // Replace the proxy reference in BlackBox's internal structures
        try {
            Field[] fields = originalProxy.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getType().isAssignableFrom(wrappedProxy.getClass())) {
                    f.set(originalProxy, wrappedProxy);
                    VCamLogger.d(TAG, "Replaced proxy field: " + f.getName());
                    break;
                }
            }
        } catch (Exception e) {
            VCamLogger.w(TAG, "Could not replace proxy field: " + e.getMessage());
        }
    }
}
