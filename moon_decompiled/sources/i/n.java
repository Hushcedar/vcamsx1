package i;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncRequest;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface n extends IInterface {

    public static abstract class a extends Binder implements n {

        /* JADX INFO: renamed from: i.n$a$a, reason: collision with other inner class name */
        public static class C0031a implements n {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static n f423b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f424a;

            static {
                perationCompat.init0(445);
            }

            public C0031a(IBinder iBinder) {
                this.f424a = iBinder;
            }

            @Override // i.n
            public native void A3(int i4, IBinder iBinder);

            @Override // i.n
            public native boolean D(Account account, String str, ComponentName componentName);

            public native String E3();

            @Override // i.n
            public native void G(SyncRequest syncRequest);

            @Override // i.n
            public native SyncAdapterType[] I(int i4);

            @Override // i.n
            public native void J2(Account account, String str, Bundle bundle, long j4);

            @Override // i.n
            public native void M(Account account, String str, int i4);

            @Override // i.n
            public native void N(Uri uri, boolean z3, IBinder iBinder, int i4, int i5);

            @Override // i.n
            public native boolean O2(Account account, String str, ComponentName componentName, int i4);

            @Override // i.n
            public native boolean P0();

            @Override // i.n
            public native Bundle Q0(String str, Uri uri, int i4);

            @Override // i.n
            public native void S(Account account, String str, boolean z3, int i4);

            @Override // i.n
            public native boolean S1(Account account, String str, ComponentName componentName);

            @Override // i.n
            public native void T(Account account, String str, Bundle bundle);

            @Override // i.n
            public native void U2(Account account, String str, ComponentName componentName);

            @Override // i.n
            public native List<PeriodicSync> Y(Account account, String str, ComponentName componentName);

            @Override // i.n
            public native void Z2(Account account, String str, ComponentName componentName, int i4);

            @Override // i.n
            public native void a3(String str, Uri uri, Bundle bundle, int i4);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.n
            public native void d2(boolean z3);

            @Override // i.n
            public native void d3(SyncRequest syncRequest, int i4);

            @Override // i.n
            public native List<m3.c> e1();

            @Override // i.n
            public native List<m3.c> f0(int i4);

            @Override // i.n
            public native String[] g2(String str, int i4);

            @Override // i.n
            public native void j0(Account account, String str, boolean z3);

            @Override // i.n
            public native boolean k(Account account, String str);

            @Override // i.n
            public native void k0(Account account, String str, Bundle bundle);

            @Override // i.n
            public native void m(boolean z3, int i4);

            @Override // i.n
            public native void m1(IBinder iBinder);

            @Override // i.n
            public native int n1(Account account, String str);

            @Override // i.n
            public native int o3(Account account, String str, int i4);

            @Override // i.n
            public native void s0(SyncRequest syncRequest);

            @Override // i.n
            public native SyncAdapterType[] s2();

            @Override // i.n
            public native boolean t(int i4);

            @Override // i.n
            public native void t1(Uri uri, IBinder iBinder, boolean z3, int i4, int i5, int i6);

            @Override // i.n
            public native boolean y(Account account, String str, int i4);

            @Override // i.n
            public native void y0(IBinder iBinder);
        }

        static {
            perationCompat.init0(576);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHContentService");
        }

        public static native n E3(IBinder iBinder);

        public static native n F3();

        public static native boolean G3(n nVar);

        @Override // i.n
        public abstract /* synthetic */ void A3(int i4, IBinder iBinder);

        @Override // i.n
        public abstract /* synthetic */ boolean D(Account account, String str, ComponentName componentName);

        @Override // i.n
        public abstract /* synthetic */ void G(SyncRequest syncRequest);

        @Override // i.n
        public abstract /* synthetic */ SyncAdapterType[] I(int i4);

        @Override // i.n
        public abstract /* synthetic */ void J2(Account account, String str, Bundle bundle, long j4);

        @Override // i.n
        public abstract /* synthetic */ void M(Account account, String str, int i4);

        @Override // i.n
        public abstract /* synthetic */ void N(Uri uri, boolean z3, IBinder iBinder, int i4, int i5);

        @Override // i.n
        public abstract /* synthetic */ boolean O2(Account account, String str, ComponentName componentName, int i4);

        @Override // i.n
        public abstract /* synthetic */ boolean P0();

        @Override // i.n
        public abstract /* synthetic */ Bundle Q0(String str, Uri uri, int i4);

        @Override // i.n
        public abstract /* synthetic */ void S(Account account, String str, boolean z3, int i4);

        @Override // i.n
        public abstract /* synthetic */ boolean S1(Account account, String str, ComponentName componentName);

        @Override // i.n
        public abstract /* synthetic */ void T(Account account, String str, Bundle bundle);

        @Override // i.n
        public abstract /* synthetic */ void U2(Account account, String str, ComponentName componentName);

        @Override // i.n
        public abstract /* synthetic */ List<PeriodicSync> Y(Account account, String str, ComponentName componentName);

        @Override // i.n
        public abstract /* synthetic */ void Z2(Account account, String str, ComponentName componentName, int i4);

        @Override // i.n
        public abstract /* synthetic */ void a3(String str, Uri uri, Bundle bundle, int i4);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.n
        public abstract /* synthetic */ void d2(boolean z3);

        @Override // i.n
        public abstract /* synthetic */ void d3(SyncRequest syncRequest, int i4);

        @Override // i.n
        public abstract /* synthetic */ List<m3.c> e1();

        @Override // i.n
        public abstract /* synthetic */ List<m3.c> f0(int i4);

        @Override // i.n
        public abstract /* synthetic */ String[] g2(String str, int i4);

        @Override // i.n
        public abstract /* synthetic */ void j0(Account account, String str, boolean z3);

        @Override // i.n
        public abstract /* synthetic */ boolean k(Account account, String str);

        @Override // i.n
        public abstract /* synthetic */ void k0(Account account, String str, Bundle bundle);

        @Override // i.n
        public abstract /* synthetic */ void m(boolean z3, int i4);

        @Override // i.n
        public abstract /* synthetic */ void m1(IBinder iBinder);

        @Override // i.n
        public abstract /* synthetic */ int n1(Account account, String str);

        @Override // i.n
        public abstract /* synthetic */ int o3(Account account, String str, int i4);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.n
        public abstract /* synthetic */ void s0(SyncRequest syncRequest);

        @Override // i.n
        public abstract /* synthetic */ SyncAdapterType[] s2();

        @Override // i.n
        public abstract /* synthetic */ boolean t(int i4);

        @Override // i.n
        public abstract /* synthetic */ void t1(Uri uri, IBinder iBinder, boolean z3, int i4, int i5, int i6);

        @Override // i.n
        public abstract /* synthetic */ boolean y(Account account, String str, int i4);

        @Override // i.n
        public abstract /* synthetic */ void y0(IBinder iBinder);
    }

    void A3(int i4, IBinder iBinder);

    boolean D(Account account, String str, ComponentName componentName);

    void G(SyncRequest syncRequest);

    SyncAdapterType[] I(int i4);

    void J2(Account account, String str, Bundle bundle, long j4);

    void M(Account account, String str, int i4);

    void N(Uri uri, boolean z3, IBinder iBinder, int i4, int i5);

    boolean O2(Account account, String str, ComponentName componentName, int i4);

    boolean P0();

    Bundle Q0(String str, Uri uri, int i4);

    void S(Account account, String str, boolean z3, int i4);

    boolean S1(Account account, String str, ComponentName componentName);

    void T(Account account, String str, Bundle bundle);

    void U2(Account account, String str, ComponentName componentName);

    List<PeriodicSync> Y(Account account, String str, ComponentName componentName);

    void Z2(Account account, String str, ComponentName componentName, int i4);

    void a3(String str, Uri uri, Bundle bundle, int i4);

    void d2(boolean z3);

    void d3(SyncRequest syncRequest, int i4);

    List<m3.c> e1();

    List<m3.c> f0(int i4);

    String[] g2(String str, int i4);

    void j0(Account account, String str, boolean z3);

    boolean k(Account account, String str);

    void k0(Account account, String str, Bundle bundle);

    void m(boolean z3, int i4);

    void m1(IBinder iBinder);

    int n1(Account account, String str);

    int o3(Account account, String str, int i4);

    void s0(SyncRequest syncRequest);

    SyncAdapterType[] s2();

    boolean t(int i4);

    void t1(Uri uri, IBinder iBinder, boolean z3, int i4, int i5, int i6);

    boolean y(Account account, String str, int i4);

    void y0(IBinder iBinder);
}
