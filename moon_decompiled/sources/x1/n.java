package x1;

import android.app.Application;
import android.app.Instrumentation;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getClassLoader", value = {})
    public static v1.f<ClassLoader> f1837a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "makeApplication", value = {boolean.class, Instrumentation.class})
    public static v1.f<Application> f1838b;

    static {
        j.e.q(n.class, "android.app.LoadedApk");
    }
}
