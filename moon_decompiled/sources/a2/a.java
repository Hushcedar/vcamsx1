package a2;

import j.e;
import v1.b;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @b(name = "sCallbacks")
    private static g<Object> f14a;

    static {
        e.q(a.class, "android.compat.Compatibility");
    }

    public static Object a() {
        g<Object> gVar = f14a;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }
}
