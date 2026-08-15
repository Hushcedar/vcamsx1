package x1;

import android.content.pm.PackageManager;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1819a = j.e.q(f.class, "android.app.ApplicationPackageManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mPM")
    private static v1.c<Object> f1820b;

    public static Object a(PackageManager packageManager) {
        v1.c<Object> cVar = f1820b;
        if (cVar != null) {
            return cVar.a(packageManager);
        }
        return null;
    }

    public static void b(PackageManager packageManager, IInterface iInterface) {
        v1.c<Object> cVar = f1820b;
        if (cVar != null) {
            cVar.b(packageManager, iInterface);
        }
    }
}
