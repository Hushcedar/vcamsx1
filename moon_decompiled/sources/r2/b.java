package r2;

import android.telephony.SubscriptionManager;
import j.e;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "sPhoneIdCache")
    private static g<Object> f1306a;

    static {
        e.r(b.class, SubscriptionManager.class);
    }

    public static Object a() {
        g<Object> gVar = f1306a;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }
}
