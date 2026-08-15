package f2;

import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "setAppName", value = {String.class})
    private static h<Object> f362a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "setAppName", value = {String.class, int.class})
    private static h<Object> f363b;

    static {
        j.e.q(a.class, "android.ddm.DdmHandleAppName");
    }

    public static void a(String str) {
        h<Object> hVar = f362a;
        if (hVar != null) {
            hVar.a(new Object[]{str});
            return;
        }
        h<Object> hVar2 = f363b;
        if (hVar2 != null) {
            hVar2.a(new Object[]{str, 0});
        }
    }
}
