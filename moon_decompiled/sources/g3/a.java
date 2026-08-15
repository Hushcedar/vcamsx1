package g3;

import v1.e;
import v1.f;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "bundleMgrProxy")
    private static g f383a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "initBundleMgrProxy", value = {})
    private static f f384b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final Class f385c = j.e.q(a.class, "ohos.abilityshell.BundleMgrBridge");

    public static Object a() {
        g gVar = f383a;
        if (gVar == null) {
            return null;
        }
        return gVar.a();
    }

    public static void b(Object obj) {
        g gVar = f383a;
        if (gVar != null) {
            gVar.b(obj);
        }
    }

    public static void c(Object obj) {
        f fVar = f384b;
        if (fVar == null) {
            return;
        }
        ((Boolean) fVar.a(obj, null)).booleanValue();
    }

    public static boolean d() {
        return (f385c == null || f383a == null || f384b == null) ? false : true;
    }
}
