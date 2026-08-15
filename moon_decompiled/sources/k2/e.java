package k2;

import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getService", value = {String.class})
    private static v1.f<IBinder> f692a;

    static {
        j.e.q(e.class, "android.os.IServiceManager");
    }

    public static IBinder a(IInterface iInterface, String str) {
        v1.f<IBinder> fVar = f692a;
        if (fVar != null) {
            return fVar.a(iInterface, new Object[]{str});
        }
        return null;
    }
}
