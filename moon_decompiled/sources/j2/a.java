package j2;

import android.net.wifi.rtt.WifiRttManager;
import android.os.IInterface;
import j.e;
import v1.b;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @b(name = "mService")
    private static c<IInterface> f549a;

    static {
        e.q(a.class, "android.net.wifi.rtt.WifiRttManager");
    }

    public static IInterface a(WifiRttManager wifiRttManager) {
        c<IInterface> cVar = f549a;
        if (cVar != null) {
            return cVar.a(wifiRttManager);
        }
        return null;
    }
}
