package i;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import androidx.core.os.perationCompat;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f410c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public q f411a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f412b;

    public class a extends v3.k<h> {
        static {
            perationCompat.init0(323);
        }

        @Override // v3.k
        public final native h a();
    }

    static {
        perationCompat.init0(50);
        f410c = new a();
    }

    public static native int W(int i4);

    public static native String X(int i4);

    public static native String Y(int i4, String str);

    public static native int e(int i4);

    public static native String f(int i4);

    public static native String g(int i4, String str);

    public static native void h(Exception exc);

    public static native void i(ApplicationInfo applicationInfo);

    public static native boolean j(ComponentInfo componentInfo);

    public static native void k(PackageInfo packageInfo);

    public static native boolean m(ResolveInfo resolveInfo);

    public static native void n(List list);

    public static native h o();

    public native Intent A(String str, int i4);

    public native boolean A0(String str, int i4, int i5, boolean z3);

    public native String B(int i4, int i5);

    public native void B0(long j4);

    @Deprecated
    public native int C(String str, int i4);

    public native PackageInfo D(int i4, String str, int i5);

    public native IBinder E();

    public native r3.s F(String str, int i4);

    public native Bundle G(String str, int i4, int i5);

    public native int H(String str);

    public native int I(String str, int i4);

    public native String[] J(int i4, int i5, int i6);

    public native PermissionGroupInfo K(String str, int i4, int i5);

    public native PermissionInfo L(String str, int i4);

    public native PermissionInfo M(String str, int i4, int i5);

    public native ProviderInfo N(ComponentName componentName, int i4, int i5);

    public native ActivityInfo O(ComponentName componentName, int i4, int i5);

    public native ServiceInfo P(ComponentName componentName, int i4, int i5);

    public native List<SharedLibraryInfo> Q(String str, int i4, int i5);

    public native PackageInfo R(String str, int i4);

    public native Map S(int i4, int i5);

    public native boolean T(String str);

    public native void U(Context context);

    public native int V(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr);

    public native boolean Z(String str);

    public native int a(String str, String str2, int i4);

    public native boolean a0(String str, int i4);

    public native int b(String str, String str2, int i4);

    public native boolean b0(String str, int i4, int i5);

    public native int c(int i4, String str, int i5);

    public native boolean c0(String str);

    public native int d(int i4, String str, int i5);

    public native boolean d0(String str, int i4);

    public native boolean e0(String str);

    public native List<ProviderInfo> f0(String str, int i4, int i5, int i6);

    public native List g0(Intent intent, String str, int i4, int i5);

    public native List<ResolveInfo> h0(Intent intent, String str, int i4, int i5);

    public native List<ResolveInfo> i0(Intent intent, String str, int i4, int i5);

    public native List<ResolveInfo> j0(Intent intent, String str, int i4, int i5);

    public native List k0(String str, int i4, int i5);

    public native void l(List<ProviderInfo> list);

    public native void l0(s sVar);

    public native void m0(t tVar);

    public native boolean n0(String str);

    public native ProviderInfo o0(String str, int i4);

    public native ActivityInfo p(ComponentName componentName, int i4, int i5);

    public native ProviderInfo p0(String str, int i4, int i5);

    public native List q(int i4, int i5);

    public native ResolveInfo q0(Intent intent, String str, int i4, int i5);

    public native int[] r();

    public native ResolveInfo r0(Intent intent, int i4);

    public native int[] s(String str);

    public native ResolveInfo s0(Intent intent, int i4);

    public native ApplicationInfo t(String str, int i4, int i5);

    public native ResolveInfo t0(Intent intent, String str, int i4, int i5);

    public native String u(String str);

    public native ServiceInfo u0(Intent intent, String str, int i4, int i5);

    public native int v(ComponentName componentName, int i4);

    public native void v0(int i4, String str, int i5, int i6);

    public native List<ApplicationInfo> w(int i4, int i5);

    public native void w0(ComponentName componentName, int i4, int i5, int i6);

    public native List<PackageInfo> x(int i4, int i5);

    public native int x0(int i4, String str, int i5);

    public native List y(int i4, int i5);

    public native void y0(s sVar);

    public native List<IntentFilter> z(ComponentName componentName, int i4);

    public native void z0(t tVar);
}
