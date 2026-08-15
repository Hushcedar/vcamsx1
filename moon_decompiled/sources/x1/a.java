package x1;

import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getDefault", value = {})
    private static v1.h<IInterface> f1772a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static IInterface f1773b;

    static {
        j.e.q(a.class, "android.app.ActivityManagerNative");
    }

    public static IInterface a() {
        IInterface iInterface = f1773b;
        if (iInterface != null) {
            return iInterface;
        }
        v1.h<IInterface> hVar = f1772a;
        if (hVar != null) {
            f1773b = hVar.a(null);
        }
        return f1773b;
    }
}
