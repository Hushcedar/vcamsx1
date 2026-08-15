package z1;

import android.app.servertransaction.ClientTransactionItem;
import android.os.IBinder;
import j.e;
import java.util.ArrayList;
import java.util.List;
import v1.f;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1899a = e.q(a.class, "android.app.servertransaction.ClientTransaction");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mActivityCallbacks")
    private static v1.c<List<ClientTransactionItem>> f1900b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "getActivityToken", value = {})
    private static f<IBinder> f1901c;

    public static IBinder a(Object obj) {
        f<IBinder> fVar = f1901c;
        if (fVar != null) {
            return fVar.a(obj, e.f514r);
        }
        return null;
    }

    public static List<ClientTransactionItem> b(Object obj) {
        v1.c<List<ClientTransactionItem>> cVar = f1900b;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }

    public static void c(Object obj, ArrayList arrayList) {
        v1.c<List<ClientTransactionItem>> cVar = f1900b;
        if (cVar != null) {
            cVar.b(obj, arrayList);
        }
    }
}
