package x1;

import android.content.pm.ProviderInfo;
import android.os.IInterface;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "info")
    private static v1.c<ProviderInfo> f1822a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "provider")
    private static v1.c<IInterface> f1823b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static v1.g<Parcelable.Creator> f1824c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static Parcelable.Creator f1825d;

    static {
        j.e.q(h.class, t1.c.a() ? "android.app.ContentProviderHolder" : "android.app.IActivityManager$ContentProviderHolder");
    }

    public static Parcelable.Creator a() {
        if (f1825d == null) {
            f1825d = f1824c.a();
        }
        return f1825d;
    }

    public static ProviderInfo b(Object obj) {
        v1.c<ProviderInfo> cVar = f1822a;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }

    public static void c(Parcelable parcelable, ProviderInfo providerInfo) {
        v1.c<ProviderInfo> cVar = f1822a;
        if (cVar != null) {
            cVar.b(parcelable, providerInfo);
        }
    }

    public static IInterface d(Parcelable parcelable) {
        v1.c<IInterface> cVar = f1823b;
        if (cVar != null) {
            return cVar.a(parcelable);
        }
        return null;
    }

    public static void e(Parcelable parcelable, IInterface iInterface) {
        v1.c<IInterface> cVar = f1823b;
        if (cVar != null) {
            cVar.b(parcelable, iInterface);
        }
    }
}
