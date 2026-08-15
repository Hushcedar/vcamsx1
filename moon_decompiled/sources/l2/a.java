package l2;

import v1.b;
import v1.e;
import v1.g;
import v1.h;
import x1.t;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "disablePermissionCache", value = {})
    public static h<Object> f1031a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @b(name = "sPermissionCache")
    private static g<Object> f1032b;

    static {
        j.e.q(a.class, "android.permission.PermissionManager");
    }

    public static void a() {
        boolean z3;
        h<Object> hVar;
        Object objA;
        g<Object> gVar = f1032b;
        if (gVar == null || (objA = gVar.a()) == null) {
            z3 = false;
        } else {
            t.a(objA);
            z3 = true;
        }
        if (z3 || (hVar = f1031a) == null) {
            return;
        }
        hVar.a(new Object[0]);
    }
}
