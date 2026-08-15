package x1;

import android.app.Application;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "collectActivityLifecycleCallbacks", value = {})
    private static v1.f<Object[]> f1821a;

    static {
        j.e.r(g.class, Application.class);
    }

    public static Object[] a(Application application) {
        v1.f<Object[]> fVar = f1821a;
        if (fVar != null) {
            return fVar.a(application, j.e.f514r);
        }
        return null;
    }
}
