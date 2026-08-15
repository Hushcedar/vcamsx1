package r2;

import android.telephony.TelephonyManager;
import v1.e;
import v1.f;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mSubId")
    private static v1.c<Integer> f1307a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "getPhoneId", value = {})
    private static f<Integer> f1308b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @e(name = "getSubId", value = {})
    private static f<Integer> f1309c;

    static {
        j.e.q(c.class, "android.telephony.TelephonyManager");
    }

    public static int a(TelephonyManager telephonyManager) {
        f<Integer> fVar = f1308b;
        if (fVar != null) {
            return fVar.a(telephonyManager, j.e.f514r).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public static int b(TelephonyManager telephonyManager) {
        f<Integer> fVar = f1309c;
        if (fVar != null) {
            return fVar.a(telephonyManager, j.e.f514r).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public static void c(TelephonyManager telephonyManager, int i4) {
        v1.c<Integer> cVar = f1307a;
        if (cVar != null) {
            cVar.b(telephonyManager, Integer.valueOf(i4));
        }
    }
}
