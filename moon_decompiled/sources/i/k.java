package i;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface k extends IInterface {

    public static abstract class a extends Binder implements k {

        /* JADX INFO: renamed from: i.k$a$a, reason: collision with other inner class name */
        public static class C0028a implements k {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static k f417b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f418a;

            static {
                perationCompat.init0(656);
            }

            public C0028a(IBinder iBinder) {
                this.f418a = iBinder;
            }

            @Override // i.k
            public native void A0(IBinder iBinder, Account account, boolean z3, int i4);

            @Override // i.k
            public native boolean A2(Account account, String str, Bundle bundle, Map map);

            @Override // i.k
            public native boolean B1(Account account);

            @Override // i.k
            public native void C(String[] strArr, String str);

            @Override // i.k
            public native Map D2(Account account);

            @Override // i.k
            public native Map E1(String str, String str2);

            public native String E3();

            @Override // i.k
            public native void I0(IBinder iBinder, Account account, String[] strArr, String str);

            @Override // i.k
            public native boolean J0(Account account, String str, Bundle bundle);

            @Override // i.k
            public native void K1(IBinder iBinder, String str, boolean z3);

            @Override // i.k
            public native String K2(Account account, String str);

            @Override // i.k
            public native String O1(Account account, String str);

            @Override // i.k
            public native boolean U0(Account account, String str, int i4);

            @Override // i.k
            public native Account[] U1(String str, String str2, String str3);

            @Override // i.k
            public native void V1(IBinder iBinder, Account account, String str, boolean z3, boolean z4, Bundle bundle);

            @Override // i.k
            public native void Y0(IBinder iBinder, String str, String str2, String[] strArr, boolean z3, Bundle bundle);

            @Override // i.k
            public native int a2(Account account, String str);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.k
            public native void b0(IBinder iBinder, Account account, Bundle bundle, boolean z3, int i4);

            @Override // i.k
            public native void b1(IBinder iBinder, Account account, String str, boolean z3, Bundle bundle);

            @Override // i.k
            public native void c(Account account, String str);

            @Override // i.k
            public native void c2(IBinder iBinder, Account account, String str);

            @Override // i.k
            public native void d(String str, String str2);

            @Override // i.k
            public native void e2(IBinder iBinder, String str, String[] strArr, String str2);

            @Override // i.k
            public native boolean f(Account account);

            @Override // i.k
            public native void g1(String[] strArr, String str);

            @Override // i.k
            public native Account[] m2(String str, int i4, String str2);

            @Override // i.k
            public native String m3(Account account);

            @Override // i.k
            public native String p(Account account);

            @Override // i.k
            public native void s(IBinder iBinder, String str, String str2);

            @Override // i.k
            public native void t2(Account account);

            @Override // i.k
            public native void v1(Account account, String str, String str2);

            @Override // i.k
            public native void y1(Account account, String str, String str2);

            @Override // i.k
            public native AuthenticatorDescription[] y2(int i4);
        }

        static {
            perationCompat.init0(389);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IAccountManager");
        }

        public static native k E3(IBinder iBinder);

        public static native k F3();

        public static native boolean G3(k kVar);

        @Override // i.k
        public abstract /* synthetic */ void A0(IBinder iBinder, Account account, boolean z3, int i4);

        @Override // i.k
        public abstract /* synthetic */ boolean A2(Account account, String str, Bundle bundle, Map map);

        @Override // i.k
        public abstract /* synthetic */ boolean B1(Account account);

        @Override // i.k
        public abstract /* synthetic */ void C(String[] strArr, String str);

        @Override // i.k
        public abstract /* synthetic */ Map D2(Account account);

        @Override // i.k
        public abstract /* synthetic */ Map E1(String str, String str2);

        @Override // i.k
        public abstract /* synthetic */ void I0(IBinder iBinder, Account account, String[] strArr, String str);

        @Override // i.k
        public abstract /* synthetic */ boolean J0(Account account, String str, Bundle bundle);

        @Override // i.k
        public abstract /* synthetic */ void K1(IBinder iBinder, String str, boolean z3);

        @Override // i.k
        public abstract /* synthetic */ String K2(Account account, String str);

        @Override // i.k
        public abstract /* synthetic */ String O1(Account account, String str);

        @Override // i.k
        public abstract /* synthetic */ boolean U0(Account account, String str, int i4);

        @Override // i.k
        public abstract /* synthetic */ Account[] U1(String str, String str2, String str3);

        @Override // i.k
        public abstract /* synthetic */ void V1(IBinder iBinder, Account account, String str, boolean z3, boolean z4, Bundle bundle);

        @Override // i.k
        public abstract /* synthetic */ void Y0(IBinder iBinder, String str, String str2, String[] strArr, boolean z3, Bundle bundle);

        @Override // i.k
        public abstract /* synthetic */ int a2(Account account, String str);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.k
        public abstract /* synthetic */ void b0(IBinder iBinder, Account account, Bundle bundle, boolean z3, int i4);

        @Override // i.k
        public abstract /* synthetic */ void b1(IBinder iBinder, Account account, String str, boolean z3, Bundle bundle);

        @Override // i.k
        public abstract /* synthetic */ void c(Account account, String str);

        @Override // i.k
        public abstract /* synthetic */ void c2(IBinder iBinder, Account account, String str);

        @Override // i.k
        public abstract /* synthetic */ void d(String str, String str2);

        @Override // i.k
        public abstract /* synthetic */ void e2(IBinder iBinder, String str, String[] strArr, String str2);

        @Override // i.k
        public abstract /* synthetic */ boolean f(Account account);

        @Override // i.k
        public abstract /* synthetic */ void g1(String[] strArr, String str);

        @Override // i.k
        public abstract /* synthetic */ Account[] m2(String str, int i4, String str2);

        @Override // i.k
        public abstract /* synthetic */ String m3(Account account);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.k
        public abstract /* synthetic */ String p(Account account);

        @Override // i.k
        public abstract /* synthetic */ void s(IBinder iBinder, String str, String str2);

        @Override // i.k
        public abstract /* synthetic */ void t2(Account account);

        @Override // i.k
        public abstract /* synthetic */ void v1(Account account, String str, String str2);

        @Override // i.k
        public abstract /* synthetic */ void y1(Account account, String str, String str2);

        @Override // i.k
        public abstract /* synthetic */ AuthenticatorDescription[] y2(int i4);
    }

    void A0(IBinder iBinder, Account account, boolean z3, int i4);

    boolean A2(Account account, String str, Bundle bundle, Map map);

    boolean B1(Account account);

    void C(String[] strArr, String str);

    Map D2(Account account);

    Map E1(String str, String str2);

    void I0(IBinder iBinder, Account account, String[] strArr, String str);

    boolean J0(Account account, String str, Bundle bundle);

    void K1(IBinder iBinder, String str, boolean z3);

    String K2(Account account, String str);

    String O1(Account account, String str);

    boolean U0(Account account, String str, int i4);

    Account[] U1(String str, String str2, String str3);

    void V1(IBinder iBinder, Account account, String str, boolean z3, boolean z4, Bundle bundle);

    void Y0(IBinder iBinder, String str, String str2, String[] strArr, boolean z3, Bundle bundle);

    int a2(Account account, String str);

    void b0(IBinder iBinder, Account account, Bundle bundle, boolean z3, int i4);

    void b1(IBinder iBinder, Account account, String str, boolean z3, Bundle bundle);

    void c(Account account, String str);

    void c2(IBinder iBinder, Account account, String str);

    void d(String str, String str2);

    void e2(IBinder iBinder, String str, String[] strArr, String str2);

    boolean f(Account account);

    void g1(String[] strArr, String str);

    Account[] m2(String str, int i4, String str2);

    String m3(Account account);

    String p(Account account);

    void s(IBinder iBinder, String str, String str2);

    void t2(Account account);

    void v1(Account account, String str, String str2);

    void y1(Account account, String str, String str2);

    AuthenticatorDescription[] y2(int i4);
}
