package p2;

import java.util.Arrays;
import java.util.List;
import k2.k;
import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "operator_iso_country", value = {})
    private static h<List<String>> f1197a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "icc_operator_iso_country", value = {})
    private static h<List<String>> f1198b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @e(name = "operator_alpha", value = {})
    private static h<List<String>> f1199c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @e(name = "operator_numeric", value = {})
    private static h<List<String>> f1200d;

    static {
        j.e.q(a.class, "android.sysprop.TelephonyProperties");
    }

    public static List<String> a(String str) {
        h<String> hVar = k.f701a;
        String strA = hVar != null ? hVar.a(new Object[]{str}) : null;
        if (strA == null || strA.length() <= 0) {
            return null;
        }
        return Arrays.asList(strA.split(","));
    }

    public static List<String> b() {
        h<List<String>> hVar = f1198b;
        return hVar != null ? hVar.a(j.e.f514r) : a("gsm.sim.operator.iso-country");
    }

    public static List<String> c() {
        h<List<String>> hVar = f1199c;
        return hVar != null ? hVar.a(j.e.f514r) : a("gsm.operator.alpha");
    }

    public static List<String> d() {
        h<List<String>> hVar = f1197a;
        return hVar != null ? hVar.a(j.e.f514r) : a("gsm.operator.iso-country");
    }

    public static List<String> e() {
        h<List<String>> hVar = f1200d;
        return hVar != null ? hVar.a(j.e.f514r) : a("gsm.operator.numeric");
    }
}
