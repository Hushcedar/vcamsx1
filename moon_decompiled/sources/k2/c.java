package k2;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "setDefusable", value = {boolean.class})
    private static v1.f<Void> f689a;

    static {
        j.e.r(c.class, Bundle.class);
    }

    public static void a(Bundle bundle) {
        v1.f<Void> fVar = f689a;
        if (fVar != null) {
            fVar.a(bundle, new Object[]{Boolean.TRUE});
        }
    }
}
