package g3;

import j.e;
import v1.d;
import v1.f;
import v1.i;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @d(name = "onBundleUpdated", value = {@i(strings = {"ohos.bundle.BundleInfo"}, type = 1)})
    private static f f386a;

    static {
        if (e.q(b.class, "ohos.abilityshell.HarmonyLoader$ApplicationChangeReceive") == null) {
            e.q(b.class, "ohos.abilityshell.HarmonyLoader$O000000");
        }
    }

    public static void a(Object obj, Object obj2) {
        f fVar = f386a;
        if (fVar != null) {
            fVar.a(obj, new Object[]{obj2});
        }
    }
}
