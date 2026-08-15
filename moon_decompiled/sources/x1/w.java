package x1;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public final class w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "", value = {boolean.class, int.class, int.class, Intent.class})
    private static v1.a<Object> f1858a;

    static {
        j.e.q(w.class, "android.app.ServiceStartArgs");
    }

    public static Object a(boolean z3, int i4, int i5, Intent intent) {
        v1.a<Object> aVar = f1858a;
        if (aVar != null) {
            return aVar.a(new Object[]{Boolean.valueOf(z3), Integer.valueOf(i4), Integer.valueOf(i5), intent});
        }
        return null;
    }
}
