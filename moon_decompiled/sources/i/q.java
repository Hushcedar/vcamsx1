package i;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import androidx.core.os.perationCompat;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface q extends IInterface {

    public static abstract class a extends Binder implements q {

        /* JADX INFO: renamed from: i.q$a$a, reason: collision with other inner class name */
        public static class C0034a implements q {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static q f429b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f430a;

            static {
                perationCompat.init0(369);
            }

            public C0034a(IBinder iBinder) {
                this.f430a = iBinder;
            }

            @Override // i.q
            public native PermissionGroupInfo C0(String str, int i4, int i5);

            @Override // i.q
            public native int C3(int i4, String str, int i5);

            @Override // i.q
            public native IBinder D1(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native IBinder E(int i4, int i5);

            public native String E3();

            @Override // i.q
            public native PackageInfo F(String str, int i4);

            @Override // i.q
            public native ActivityInfo F2(ComponentName componentName, int i4, int i5);

            @Override // i.q
            public native boolean H0(String str);

            @Override // i.q
            public native void H2(ComponentName componentName, int i4, int i5, int i6);

            @Override // i.q
            public native PermissionInfo I1(String str, int i4, int i5);

            @Override // i.q
            public native List J(ComponentName componentName, int i4);

            @Override // i.q
            public native int K0(String str, String str2, int i4);

            @Override // i.q
            public native void L(long j4);

            @Override // i.q
            public native ActivityInfo L1(ComponentName componentName, int i4, int i5);

            @Override // i.q
            public native String N0(int i4, int i5);

            @Override // i.q
            public native int N1(String str, String str2, int i4, String str3);

            @Override // i.q
            public native int[] O0();

            @Override // i.q
            public native ProviderInfo P1(ComponentName componentName, int i4, int i5);

            @Override // i.q
            public native List<SharedLibraryInfo> Q(String str, int i4, int i5);

            @Override // i.q
            public native IBinder Q2(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native IBinder S2(int i4, int i5);

            @Override // i.q
            public native IBinder T0(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native boolean U(String str, int i4);

            @Override // i.q
            public native ResolveInfo V(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native ResolveInfo X(Intent intent, int i4);

            @Override // i.q
            public native ApplicationInfo X0(String str, int i4, int i5);

            @Override // i.q
            public native r3.s X1(String str, int i4);

            @Override // i.q
            public native IBinder Z1(int i4, int i5);

            @Override // i.q
            public native void a(t tVar);

            @Override // i.q
            public native PackageInfo a0(int i4, String str, int i5);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.q
            public native ProviderInfo b(String str, String str2, int i4, int i5);

            @Override // i.q
            public native int[] b3(String str);

            @Override // i.q
            public native boolean d0(String str);

            @Override // i.q
            public native boolean e3(String str, int i4);

            @Override // i.q
            public native String[] g(int i4, int i5, int i6);

            @Override // i.q
            public native ResolveInfo h1(Intent intent, int i4);

            @Override // i.q
            public native Map h3(int i4, int i5);

            @Override // i.q
            public native IBinder i0();

            @Override // i.q
            public native ResolveInfo i1(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native int l2(String str, int i4);

            @Override // i.q
            public native int n(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr);

            @Override // i.q
            public native ServiceInfo o(ComponentName componentName, int i4, int i5);

            @Override // i.q
            public native List o1(Intent intent, String str, int i4, int i5);

            @Override // i.q
            public native IBinder o2(String str, int i4, int i5, int i6);

            @Override // i.q
            public native int p2(String str, int i4);

            @Override // i.q
            public native boolean q1(String str);

            @Override // i.q
            public native Bundle r(String str, int i4, int i5);

            @Override // i.q
            public native void t3(s sVar);

            @Override // i.q
            public native void u(s sVar);

            @Override // i.q
            public native IBinder u2(String str, int i4, int i5);

            @Override // i.q
            public native boolean v2(String str, int i4, int i5, boolean z3);

            @Override // i.q
            public native void v3(int i4, String str, int i5, int i6);

            @Override // i.q
            public native IBinder w0(int i4, int i5);

            @Override // i.q
            public native void x(t tVar);

            @Override // i.q
            public native int x0(ComponentName componentName, int i4);

            @Override // i.q
            public native int y3(int i4, String str, int i5);

            @Override // i.q
            public native int z0(int i4, String str, int i5);

            @Override // i.q
            public native boolean z2(String str, int i4, int i5);

            @Override // i.q
            public native String z3(String str);
        }

        static {
            perationCompat.init0(677);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHPackageManager");
        }

        public static native q E3(IBinder iBinder);

        public static native q F3();

        public static native boolean G3(q qVar);

        @Override // i.q
        public abstract /* synthetic */ PermissionGroupInfo C0(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ int C3(int i4, String str, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder D1(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder E(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ PackageInfo F(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ ActivityInfo F2(ComponentName componentName, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ boolean H0(String str);

        @Override // i.q
        public abstract /* synthetic */ void H2(ComponentName componentName, int i4, int i5, int i6);

        @Override // i.q
        public abstract /* synthetic */ PermissionInfo I1(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ List J(ComponentName componentName, int i4);

        @Override // i.q
        public abstract /* synthetic */ int K0(String str, String str2, int i4);

        @Override // i.q
        public abstract /* synthetic */ void L(long j4);

        @Override // i.q
        public abstract /* synthetic */ ActivityInfo L1(ComponentName componentName, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ String N0(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ int N1(String str, String str2, int i4, String str3);

        @Override // i.q
        public abstract /* synthetic */ int[] O0();

        @Override // i.q
        public abstract /* synthetic */ ProviderInfo P1(ComponentName componentName, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ List<SharedLibraryInfo> Q(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder Q2(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder S2(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder T0(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ boolean U(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ ResolveInfo V(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ ResolveInfo X(Intent intent, int i4);

        @Override // i.q
        public abstract /* synthetic */ ApplicationInfo X0(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ r3.s X1(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ IBinder Z1(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ void a(t tVar);

        @Override // i.q
        public abstract /* synthetic */ PackageInfo a0(int i4, String str, int i5);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.q
        public abstract /* synthetic */ ProviderInfo b(String str, String str2, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ int[] b3(String str);

        @Override // i.q
        public abstract /* synthetic */ boolean d0(String str);

        @Override // i.q
        public abstract /* synthetic */ boolean e3(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ String[] g(int i4, int i5, int i6);

        @Override // i.q
        public abstract /* synthetic */ ResolveInfo h1(Intent intent, int i4);

        @Override // i.q
        public abstract /* synthetic */ Map h3(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder i0();

        @Override // i.q
        public abstract /* synthetic */ ResolveInfo i1(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ int l2(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ int n(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr);

        @Override // i.q
        public abstract /* synthetic */ ServiceInfo o(ComponentName componentName, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ List o1(Intent intent, String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ IBinder o2(String str, int i4, int i5, int i6);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.q
        public abstract /* synthetic */ int p2(String str, int i4);

        @Override // i.q
        public abstract /* synthetic */ boolean q1(String str);

        @Override // i.q
        public abstract /* synthetic */ Bundle r(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ void t3(s sVar);

        @Override // i.q
        public abstract /* synthetic */ void u(s sVar);

        @Override // i.q
        public abstract /* synthetic */ IBinder u2(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ boolean v2(String str, int i4, int i5, boolean z3);

        @Override // i.q
        public abstract /* synthetic */ void v3(int i4, String str, int i5, int i6);

        @Override // i.q
        public abstract /* synthetic */ IBinder w0(int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ void x(t tVar);

        @Override // i.q
        public abstract /* synthetic */ int x0(ComponentName componentName, int i4);

        @Override // i.q
        public abstract /* synthetic */ int y3(int i4, String str, int i5);

        @Override // i.q
        public abstract /* synthetic */ int z0(int i4, String str, int i5);

        @Override // i.q
        public abstract /* synthetic */ boolean z2(String str, int i4, int i5);

        @Override // i.q
        public abstract /* synthetic */ String z3(String str);
    }

    PermissionGroupInfo C0(String str, int i4, int i5);

    int C3(int i4, String str, int i5);

    IBinder D1(Intent intent, String str, int i4, int i5);

    IBinder E(int i4, int i5);

    PackageInfo F(String str, int i4);

    ActivityInfo F2(ComponentName componentName, int i4, int i5);

    boolean H0(String str);

    void H2(ComponentName componentName, int i4, int i5, int i6);

    PermissionInfo I1(String str, int i4, int i5);

    List J(ComponentName componentName, int i4);

    int K0(String str, String str2, int i4);

    void L(long j4);

    ActivityInfo L1(ComponentName componentName, int i4, int i5);

    String N0(int i4, int i5);

    int N1(String str, String str2, int i4, String str3);

    int[] O0();

    ProviderInfo P1(ComponentName componentName, int i4, int i5);

    List<SharedLibraryInfo> Q(String str, int i4, int i5);

    IBinder Q2(Intent intent, String str, int i4, int i5);

    IBinder S2(int i4, int i5);

    IBinder T0(Intent intent, String str, int i4, int i5);

    boolean U(String str, int i4);

    ResolveInfo V(Intent intent, String str, int i4, int i5);

    ResolveInfo X(Intent intent, int i4);

    ApplicationInfo X0(String str, int i4, int i5);

    r3.s X1(String str, int i4);

    IBinder Z1(int i4, int i5);

    void a(t tVar);

    PackageInfo a0(int i4, String str, int i5);

    ProviderInfo b(String str, String str2, int i4, int i5);

    int[] b3(String str);

    boolean d0(String str);

    boolean e3(String str, int i4);

    String[] g(int i4, int i5, int i6);

    ResolveInfo h1(Intent intent, int i4);

    Map h3(int i4, int i5);

    IBinder i0();

    ResolveInfo i1(Intent intent, String str, int i4, int i5);

    int l2(String str, int i4);

    int n(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr);

    ServiceInfo o(ComponentName componentName, int i4, int i5);

    List o1(Intent intent, String str, int i4, int i5);

    IBinder o2(String str, int i4, int i5, int i6);

    int p2(String str, int i4);

    boolean q1(String str);

    Bundle r(String str, int i4, int i5);

    void t3(s sVar);

    void u(s sVar);

    IBinder u2(String str, int i4, int i5);

    boolean v2(String str, int i4, int i5, boolean z3);

    void v3(int i4, String str, int i5, int i6);

    IBinder w0(int i4, int i5);

    void x(t tVar);

    int x0(ComponentName componentName, int i4);

    int y3(int i4, String str, int i5);

    int z0(int i4, String str, int i5);

    boolean z2(String str, int i4, int i5);

    String z3(String str);
}
