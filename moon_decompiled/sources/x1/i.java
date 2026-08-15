package x1;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.d(name = "createAppContext", value = {@v1.i(strings = {"android.app.ActivityThread", "android.app.LoadedApk"}, type = 1)})
    private static v1.h<Context> f1826a;

    static {
        j.e.q(i.class, "android.app.ContextImpl");
    }

    public static Context a(Object obj, Object obj2) {
        try {
            v1.h<Context> hVar = f1826a;
            if (hVar != null) {
                return hVar.b(new Object[]{obj, obj2});
            }
            return null;
        } catch (Exception e4) {
            e4.printStackTrace();
            return null;
        }
    }
}
