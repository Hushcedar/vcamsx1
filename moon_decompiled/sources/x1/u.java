package x1;

import android.content.res.Configuration;

/* JADX INFO: loaded from: classes.dex */
public final class u {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getConfiguration", value = {})
    private static v1.f<Configuration> f1856a;

    static {
        j.e.q(u.class, "android.app.ResourcesManager");
    }

    public static Configuration a(Object obj) {
        v1.f<Configuration> fVar = f1856a;
        if (fVar == null || obj == null) {
            return null;
        }
        return fVar.a(obj, null);
    }
}
