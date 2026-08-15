package h3;

import v1.e;
import v1.f;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "getBundleInfo", value = {String.class, int.class})
    private static f f390a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "getInstance", value = {})
    private static h f391b;

    static {
        j.e.q(a.class, "ohos.bundle.BundleManager");
    }

    public static Object a(Object obj, String str) {
        f fVar = f390a;
        if (fVar == null) {
            return null;
        }
        return fVar.a(obj, new Object[]{str, 1});
    }

    public static Object b() {
        h hVar = f391b;
        if (hVar == null) {
            return null;
        }
        return hVar.a(null);
    }
}
