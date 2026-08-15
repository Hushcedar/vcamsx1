package i;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import t1.g;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f401b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public m f402a;

    public class a extends v3.k<d> {
        static {
            perationCompat.init0(118);
        }

        @Override // v3.k
        public final native d a();
    }

    public class b extends g.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ CountDownLatch f403a;

        static {
            perationCompat.init0(115);
        }

        public b(CountDownLatch countDownLatch) {
            this.f403a = countDownLatch;
        }

        @Override // t1.g
        public native void c1(int i4);
    }

    static {
        perationCompat.init0(44);
        f401b = new a();
    }

    public static native boolean L();

    public static final native boolean M(int i4);

    public static final native boolean N(int i4);

    public static native void h(Exception exc);

    public static native d n();

    public native int A();

    public native List<ActivityManager.RunningServiceInfo> B(int i4, int i5);

    public native String C(int i4, String str);

    public native String D(IBinder iBinder, String str);

    public native IBinder E(int i4);

    public native int F(IBinder iBinder);

    public native int G(int i4);

    public native t1.i H(int i4);

    public native boolean I(String str, int i4);

    public native boolean J(IBinder iBinder);

    public native void K(Context context);

    public native void O(String str, int i4, String str2);

    public native boolean P(int i4, IBinder iBinder);

    public native boolean Q(int i4, IBinder iBinder, IBinder iBinder2, int i5, int i6);

    public native boolean R(int i4, IBinder iBinder);

    public native boolean S(int i4, IBinder iBinder);

    public native void T(IBinder iBinder, int i4);

    public native boolean U(int i4, IBinder iBinder);

    public native IBinder V(Intent intent, String str, String str2);

    public native boolean W(IBinder iBinder, Intent intent, IBinder iBinder2);

    public native void X(int i4, String str, String str2);

    public native int Y(Activity activity, Intent intent, int i4, Intent intent2);

    public native Intent Z(String str, IBinder iBinder, IntentFilter intentFilter, int i4, int i5);

    public native boolean a(IBinder iBinder);

    public native void a0(IBinder iBinder, int i4, int i5, int i6);

    public native boolean b(f.b bVar, int i4);

    public native void b0(ComponentName componentName, IBinder iBinder, int i4, Notification notification, int i5, int i6);

    public native int c(IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, String str3, int i5);

    public native boolean c0(int i4);

    public native int d(IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, int i5);

    public native int d0(f.b bVar, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i4);

    public native boolean e(Intent intent);

    public native int e0(Intent intent, Bundle bundle, int i4, Intent intent2);

    public native void f(t1.i iVar, boolean z3);

    public native int f0(f.b bVar, String str, Intent intent, String str2, IBinder iBinder, String str3, int i4, int i5, Bundle bundle, int i6, Intent intent2, k3.i iVar);

    public native int g(String str, int i4, int i5, int i6);

    public native void g0(Intent intent, String str);

    public native void h0(int i4, String str, String str2, t1.g gVar);

    public native boolean i(int i4, IBinder iBinder, int i5, Intent intent);

    public native boolean i0(String str, int i4, int i5);

    public native boolean j(ActivityManager.RecentTaskInfo recentTaskInfo);

    public native ComponentName j0(Intent intent, String str, boolean z3, String str2, int i4);

    public native boolean k(ActivityManager.RunningAppProcessInfo runningAppProcessInfo);

    public native int k0(Intent intent, String str, int i4);

    public native boolean l(ActivityManager.RunningTaskInfo runningTaskInfo);

    public native boolean l0(ComponentName componentName, IBinder iBinder, int i4, int i5);

    public native void m(ActivityManager.RecentTaskInfo recentTaskInfo);

    public native void m0(IBinder iBinder, Intent intent, boolean z3);

    public native boolean n0(IBinder iBinder);

    public native int o(int i4);

    public native boolean o0(String str, int i4, int i5);

    public native ComponentName p(IBinder iBinder);

    public native void p0(IBinder iBinder, int i4, int i5);

    public native String q(IBinder iBinder);

    public native String r(int i4);

    public native e.d s(String str, int i4, int i5);

    public native k3.r t(IBinder iBinder);

    public native t1.i u(int i4, String str, IBinder iBinder, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7);

    public native ActivityManager.RunningAppProcessInfo v();

    public native String w(IBinder iBinder);

    public native String[] x(int i4);

    public native ComponentName y(ComponentName componentName, boolean z3);

    public native Map z(int i4);
}
