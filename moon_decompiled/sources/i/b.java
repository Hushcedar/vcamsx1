package i;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f394b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public k f395a;

    public class a extends v3.k<b> {
        static {
            perationCompat.init0(31);
        }

        @Override // v3.k
        public final native b a();
    }

    static {
        perationCompat.init0(42);
        f394b = new a();
    }

    public static native b h();

    public native boolean A(Account account);

    public native void B(IBinder iBinder, Account account, String str);

    public native boolean C(Account account, String str, int i4);

    public native void D(Account account, String str, String str2);

    public native void E(Account account, String str);

    public native void F(Account account, String str, String str2);

    public native void G(String[] strArr);

    public native void H(IBinder iBinder, Account account, String str, boolean z3, Bundle bundle);

    public native boolean a(Account account);

    public native void b(IBinder iBinder, String str, String str2, String[] strArr, boolean z3, Bundle bundle);

    public native boolean c(Account account, String str, Bundle bundle);

    public native boolean d(Account account, String str, Bundle bundle, Map<String, Integer> map);

    public native void e(Account account);

    public native void f(IBinder iBinder, Account account, Bundle bundle, boolean z3);

    public native void g(IBinder iBinder, String str, boolean z3);

    public native int i(Account account, String str);

    public native Map<String, Integer> j(String str, String str2);

    public native Account[] k(int i4);

    public native Account[] l(String str, int i4);

    public native void m(IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr);

    public native Account[] n(String str, String str2);

    public native void o(IBinder iBinder, Account account, String str, boolean z3, boolean z4, Bundle bundle);

    public native AuthenticatorDescription[] p();

    public native Map<String, Integer> q(Account account);

    public native String r(Account account);

    public native String s(Account account);

    public native String t(Account account, String str);

    public native void u(IAccountManagerResponse iAccountManagerResponse, Account account, String[] strArr);

    public native void v();

    public native void w(String str, String str2);

    public native String x(Account account, String str);

    public native void y(String[] strArr);

    public native void z(IBinder iBinder, Account account, boolean z3);
}
