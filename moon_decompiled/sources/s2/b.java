package s2;

import android.util.DisplayMetrics;
import j.e;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "DENSITY_DEVICE")
    private static c<Integer> f1478a;

    static {
        e.r(b.class, DisplayMetrics.class);
    }

    public static Integer a() {
        c<Integer> cVar = f1478a;
        if (cVar != null) {
            return cVar.a(null);
        }
        return null;
    }

    public static void b(int i4) {
        c<Integer> cVar = f1478a;
        if (cVar != null) {
            cVar.b(null, Integer.valueOf(i4));
        }
    }
}
