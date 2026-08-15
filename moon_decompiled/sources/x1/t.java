package x1;

import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mCache")
    private static v1.c<HashMap<Integer, Integer>> f1853a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "disableLocal", value = {String.class})
    public static v1.h<Object> f1854b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "clear", value = {})
    private static v1.f<Void> f1855c;

    static {
        j.e.q(t.class, "android.app.PropertyInvalidatedCache");
    }

    public static void a(Object obj) {
        v1.f<Void> fVar = f1855c;
        if (fVar != null) {
            fVar.a(obj, j.e.f514r);
        }
    }

    public static HashMap<Integer, Integer> b(Object obj) {
        v1.c<HashMap<Integer, Integer>> cVar = f1853a;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }
}
