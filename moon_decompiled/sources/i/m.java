package i;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface m extends IInterface {

    public static abstract class a extends Binder implements m {

        /* JADX INFO: renamed from: i.m$a$a, reason: collision with other inner class name */
        public static class C0030a implements m {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static m f421b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f422a;

            static {
                perationCompat.init0(696);
            }

            public C0030a(IBinder iBinder) {
                this.f422a = iBinder;
            }

            @Override // i.m
            public native int A(int i4);

            @Override // i.m
            public native IBinder A1(Intent intent, String str, String str2, int i4);

            @Override // i.m
            public native int B0(f.b bVar, Intent intent, String str, int i4);

            @Override // i.m
            public native boolean B2(String str, int i4, int i5);

            @Override // i.m
            public native void B3(IBinder iBinder, Intent intent, boolean z3);

            @Override // i.m
            public native t1.i C1(int i4, String str, IBinder iBinder, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7);

            @Override // i.m
            public native boolean D3(IBinder iBinder);

            @Override // i.m
            public native boolean E0(Intent intent, String str, int i4);

            @Override // i.m
            public native String[] E2(int i4);

            public native String E3();

            @Override // i.m
            public native int J1(int i4);

            @Override // i.m
            public native ComponentName K(IBinder iBinder);

            @Override // i.m
            public native void L0(Intent intent, String str);

            @Override // i.m
            public native void M1(String str, int i4, int i5);

            @Override // i.m
            public native boolean M2(IBinder iBinder);

            @Override // i.m
            public native boolean O(IBinder iBinder, Intent intent, IBinder iBinder2);

            @Override // i.m
            public native boolean P(int i4, IBinder iBinder, int i5, Intent intent);

            @Override // i.m
            public native int R0(String str, int i4, int i5, int i6, String str2);

            @Override // i.m
            public native void R2(ActivityManager.RecentTaskInfo recentTaskInfo);

            @Override // i.m
            public native void S0(int i4, String str, String str2, t1.g gVar);

            @Override // i.m
            public native String T1(IBinder iBinder);

            @Override // i.m
            public native int T2(IBinder iBinder);

            @Override // i.m
            public native ComponentName V2(ComponentName componentName, boolean z3);

            @Override // i.m
            public native t1.i W0(int i4);

            @Override // i.m
            public native ComponentName W2(f.b bVar, Intent intent, String str, boolean z3, String str2, int i4);

            @Override // i.m
            public native String X2(int i4, String str, int i5);

            @Override // i.m
            public native IBinder Y1(int i4, int i5, int i6);

            @Override // i.m
            public native ActivityManager.RunningAppProcessInfo Y2();

            @Override // i.m
            public native boolean Z0(IBinder iBinder, int i4);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.m
            public native void b2(IBinder iBinder, int i4, int i5, int i6);

            @Override // i.m
            public native boolean c0(String str, int i4);

            @Override // i.m
            public native k3.r c3(IBinder iBinder);

            @Override // i.m
            public native void d1(int i4, IBinder iBinder, int i5);

            @Override // i.m
            public native int e(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, String str3, int i5);

            @Override // i.m
            public native int f3();

            @Override // i.m
            public native void h2(IBinder iBinder, int i4, int i5, int i6);

            @Override // i.m
            public native int i3(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, int i5);

            @Override // i.m
            public native int j(f.b bVar, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i4);

            @Override // i.m
            public native String j1(IBinder iBinder, String str);

            @Override // i.m
            public native void j3(int i4, IBinder iBinder, IBinder iBinder2, int i5, int i6, String str);

            @Override // i.m
            public native IBinder k1(int i4);

            @Override // i.m
            public native void l1(t1.i iVar, boolean z3);

            @Override // i.m
            public native void m0(f.b bVar, int i4);

            @Override // i.m
            public native boolean n2(ComponentName componentName, IBinder iBinder, int i4, int i5);

            @Override // i.m
            public native void p3(int i4, IBinder iBinder);

            @Override // i.m
            public native void q(ComponentName componentName, IBinder iBinder, int i4, Notification notification, int i5, int i6, int i7);

            @Override // i.m
            public native void q3(int i4, String str, String str2, int i5);

            @Override // i.m
            public native String r0(int i4);

            @Override // i.m
            public native boolean r3(ActivityManager.RunningTaskInfo runningTaskInfo, int i4);

            @Override // i.m
            public native void s1(int i4, IBinder iBinder);

            @Override // i.m
            public native Intent s3(f.b bVar, String str, IBinder iBinder, IntentFilter intentFilter, int i4, int i5);

            @Override // i.m
            public native void t0(String str, int i4, int i5, String str2);

            @Override // i.m
            public native e.d u0(String str, int i4, int i5, String str2);

            @Override // i.m
            public native Map u1(int i4);

            @Override // i.m
            public native void v0(int i4, IBinder iBinder);

            @Override // i.m
            public native void w(int i4, IBinder iBinder);

            @Override // i.m
            public native String w2(IBinder iBinder);

            @Override // i.m
            public native boolean w3(ActivityManager.RunningAppProcessInfo runningAppProcessInfo, int i4);

            @Override // i.m
            public native int x1(f.b bVar, String str, Intent intent, String str2, IBinder iBinder, String str3, int i4, int i5, Bundle bundle, int i6, Intent intent2, k3.i iVar);

            @Override // i.m
            public native boolean x3(ActivityManager.RecentTaskInfo recentTaskInfo, int i4);

            @Override // i.m
            public native boolean z1(int i4, String str, int i5);
        }

        static {
            perationCompat.init0(544);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHActivityManager");
        }

        public static native m E3(IBinder iBinder);

        public static native m F3();

        public static native boolean G3(m mVar);

        @Override // i.m
        public abstract /* synthetic */ int A(int i4);

        @Override // i.m
        public abstract /* synthetic */ IBinder A1(Intent intent, String str, String str2, int i4);

        @Override // i.m
        public abstract /* synthetic */ int B0(f.b bVar, Intent intent, String str, int i4);

        @Override // i.m
        public abstract /* synthetic */ boolean B2(String str, int i4, int i5);

        @Override // i.m
        public abstract /* synthetic */ void B3(IBinder iBinder, Intent intent, boolean z3);

        @Override // i.m
        public abstract /* synthetic */ t1.i C1(int i4, String str, IBinder iBinder, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7);

        @Override // i.m
        public abstract /* synthetic */ boolean D3(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ boolean E0(Intent intent, String str, int i4);

        @Override // i.m
        public abstract /* synthetic */ String[] E2(int i4);

        @Override // i.m
        public abstract /* synthetic */ int J1(int i4);

        @Override // i.m
        public abstract /* synthetic */ ComponentName K(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ void L0(Intent intent, String str);

        @Override // i.m
        public abstract /* synthetic */ void M1(String str, int i4, int i5);

        @Override // i.m
        public abstract /* synthetic */ boolean M2(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ boolean O(IBinder iBinder, Intent intent, IBinder iBinder2);

        @Override // i.m
        public abstract /* synthetic */ boolean P(int i4, IBinder iBinder, int i5, Intent intent);

        @Override // i.m
        public abstract /* synthetic */ int R0(String str, int i4, int i5, int i6, String str2);

        @Override // i.m
        public abstract /* synthetic */ void R2(ActivityManager.RecentTaskInfo recentTaskInfo);

        @Override // i.m
        public abstract /* synthetic */ void S0(int i4, String str, String str2, t1.g gVar);

        @Override // i.m
        public abstract /* synthetic */ String T1(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ int T2(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ ComponentName V2(ComponentName componentName, boolean z3);

        @Override // i.m
        public abstract /* synthetic */ t1.i W0(int i4);

        @Override // i.m
        public abstract /* synthetic */ ComponentName W2(f.b bVar, Intent intent, String str, boolean z3, String str2, int i4);

        @Override // i.m
        public abstract /* synthetic */ String X2(int i4, String str, int i5);

        @Override // i.m
        public abstract /* synthetic */ IBinder Y1(int i4, int i5, int i6);

        @Override // i.m
        public abstract /* synthetic */ ActivityManager.RunningAppProcessInfo Y2();

        @Override // i.m
        public abstract /* synthetic */ boolean Z0(IBinder iBinder, int i4);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.m
        public abstract /* synthetic */ void b2(IBinder iBinder, int i4, int i5, int i6);

        @Override // i.m
        public abstract /* synthetic */ boolean c0(String str, int i4);

        @Override // i.m
        public abstract /* synthetic */ k3.r c3(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ void d1(int i4, IBinder iBinder, int i5);

        @Override // i.m
        public abstract /* synthetic */ int e(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, String str3, int i5);

        @Override // i.m
        public abstract /* synthetic */ int f3();

        @Override // i.m
        public abstract /* synthetic */ void h2(IBinder iBinder, int i4, int i5, int i6);

        @Override // i.m
        public abstract /* synthetic */ int i3(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, int i5);

        @Override // i.m
        public abstract /* synthetic */ int j(f.b bVar, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i4);

        @Override // i.m
        public abstract /* synthetic */ String j1(IBinder iBinder, String str);

        @Override // i.m
        public abstract /* synthetic */ void j3(int i4, IBinder iBinder, IBinder iBinder2, int i5, int i6, String str);

        @Override // i.m
        public abstract /* synthetic */ IBinder k1(int i4);

        @Override // i.m
        public abstract /* synthetic */ void l1(t1.i iVar, boolean z3);

        @Override // i.m
        public abstract /* synthetic */ void m0(f.b bVar, int i4);

        @Override // i.m
        public abstract /* synthetic */ boolean n2(ComponentName componentName, IBinder iBinder, int i4, int i5);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.m
        public abstract /* synthetic */ void p3(int i4, IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ void q(ComponentName componentName, IBinder iBinder, int i4, Notification notification, int i5, int i6, int i7);

        @Override // i.m
        public abstract /* synthetic */ void q3(int i4, String str, String str2, int i5);

        @Override // i.m
        public abstract /* synthetic */ String r0(int i4);

        @Override // i.m
        public abstract /* synthetic */ boolean r3(ActivityManager.RunningTaskInfo runningTaskInfo, int i4);

        @Override // i.m
        public abstract /* synthetic */ void s1(int i4, IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ Intent s3(f.b bVar, String str, IBinder iBinder, IntentFilter intentFilter, int i4, int i5);

        @Override // i.m
        public abstract /* synthetic */ void t0(String str, int i4, int i5, String str2);

        @Override // i.m
        public abstract /* synthetic */ e.d u0(String str, int i4, int i5, String str2);

        @Override // i.m
        public abstract /* synthetic */ Map u1(int i4);

        @Override // i.m
        public abstract /* synthetic */ void v0(int i4, IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ void w(int i4, IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ String w2(IBinder iBinder);

        @Override // i.m
        public abstract /* synthetic */ boolean w3(ActivityManager.RunningAppProcessInfo runningAppProcessInfo, int i4);

        @Override // i.m
        public abstract /* synthetic */ int x1(f.b bVar, String str, Intent intent, String str2, IBinder iBinder, String str3, int i4, int i5, Bundle bundle, int i6, Intent intent2, k3.i iVar);

        @Override // i.m
        public abstract /* synthetic */ boolean x3(ActivityManager.RecentTaskInfo recentTaskInfo, int i4);

        @Override // i.m
        public abstract /* synthetic */ boolean z1(int i4, String str, int i5);
    }

    int A(int i4);

    IBinder A1(Intent intent, String str, String str2, int i4);

    int B0(f.b bVar, Intent intent, String str, int i4);

    boolean B2(String str, int i4, int i5);

    void B3(IBinder iBinder, Intent intent, boolean z3);

    t1.i C1(int i4, String str, IBinder iBinder, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7);

    boolean D3(IBinder iBinder);

    boolean E0(Intent intent, String str, int i4);

    String[] E2(int i4);

    int J1(int i4);

    ComponentName K(IBinder iBinder);

    void L0(Intent intent, String str);

    void M1(String str, int i4, int i5);

    boolean M2(IBinder iBinder);

    boolean O(IBinder iBinder, Intent intent, IBinder iBinder2);

    boolean P(int i4, IBinder iBinder, int i5, Intent intent);

    int R0(String str, int i4, int i5, int i6, String str2);

    void R2(ActivityManager.RecentTaskInfo recentTaskInfo);

    void S0(int i4, String str, String str2, t1.g gVar);

    String T1(IBinder iBinder);

    int T2(IBinder iBinder);

    ComponentName V2(ComponentName componentName, boolean z3);

    t1.i W0(int i4);

    ComponentName W2(f.b bVar, Intent intent, String str, boolean z3, String str2, int i4);

    String X2(int i4, String str, int i5);

    IBinder Y1(int i4, int i5, int i6);

    ActivityManager.RunningAppProcessInfo Y2();

    boolean Z0(IBinder iBinder, int i4);

    void b2(IBinder iBinder, int i4, int i5, int i6);

    boolean c0(String str, int i4);

    k3.r c3(IBinder iBinder);

    void d1(int i4, IBinder iBinder, int i5);

    int e(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, String str3, int i5);

    int f3();

    void h2(IBinder iBinder, int i4, int i5, int i6);

    int i3(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, int i5);

    int j(f.b bVar, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i4);

    String j1(IBinder iBinder, String str);

    void j3(int i4, IBinder iBinder, IBinder iBinder2, int i5, int i6, String str);

    IBinder k1(int i4);

    void l1(t1.i iVar, boolean z3);

    void m0(f.b bVar, int i4);

    boolean n2(ComponentName componentName, IBinder iBinder, int i4, int i5);

    void p3(int i4, IBinder iBinder);

    void q(ComponentName componentName, IBinder iBinder, int i4, Notification notification, int i5, int i6, int i7);

    void q3(int i4, String str, String str2, int i5);

    String r0(int i4);

    boolean r3(ActivityManager.RunningTaskInfo runningTaskInfo, int i4);

    void s1(int i4, IBinder iBinder);

    Intent s3(f.b bVar, String str, IBinder iBinder, IntentFilter intentFilter, int i4, int i5);

    void t0(String str, int i4, int i5, String str2);

    e.d u0(String str, int i4, int i5, String str2);

    Map u1(int i4);

    void v0(int i4, IBinder iBinder);

    void w(int i4, IBinder iBinder);

    String w2(IBinder iBinder);

    boolean w3(ActivityManager.RunningAppProcessInfo runningAppProcessInfo, int i4);

    int x1(f.b bVar, String str, Intent intent, String str2, IBinder iBinder, String str3, int i4, int i5, Bundle bundle, int i6, Intent intent2, k3.i iVar);

    boolean x3(ActivityManager.RecentTaskInfo recentTaskInfo, int i4);

    boolean z1(int i4, String str, int i5);
}
