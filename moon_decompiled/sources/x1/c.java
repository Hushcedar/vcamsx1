package x1;

import android.app.Activity;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mCalled")
    private static v1.c<Boolean> f1781a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mToken")
    private static v1.c<IBinder> f1782b;

    static {
        j.e.r(c.class, Activity.class);
    }

    public static boolean a(Activity activity) {
        v1.c<Boolean> cVar = f1781a;
        if (cVar != null) {
            return cVar.a(activity).booleanValue();
        }
        return false;
    }

    public static IBinder b(Activity activity) {
        v1.c<IBinder> cVar = f1782b;
        if (cVar != null) {
            return cVar.a(activity);
        }
        return null;
    }
}
