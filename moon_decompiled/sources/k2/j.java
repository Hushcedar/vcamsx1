package k2;

import android.os.IBinder;
import android.os.IInterface;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getIServiceManager", value = {})
    private static v1.h<IInterface> f698a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "getService", value = {String.class})
    private static v1.h<IBinder> f699b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "sCache")
    private static v1.g<Map<String, IBinder>> f700c;

    static {
        j.e.q(j.class, "android.os.ServiceManager");
    }

    public static void a(IBinder iBinder, String str) {
        v1.g<Map<String, IBinder>> gVar = f700c;
        if (gVar != null) {
            gVar.a().put(str, iBinder);
        }
    }

    public static IInterface b() {
        v1.h<IInterface> hVar = f698a;
        if (hVar != null) {
            return hVar.a(new Object[0]);
        }
        return null;
    }

    public static IBinder c(String str) {
        v1.h<IBinder> hVar = f699b;
        if (hVar != null) {
            return hVar.a(new Object[]{str});
        }
        return null;
    }
}
