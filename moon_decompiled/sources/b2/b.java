package b2;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f65a = j.e.q(b.class, "android.content.AttributionSourceState");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "", value = {})
    public static v1.a<Object> f66b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "next")
    private static v1.c<Object[]> f67c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "packageName")
    private static v1.c<String> f68d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.b(name = "uid")
    private static v1.c<Integer> f69e;

    public static Object[] a(Object obj) {
        v1.c<Object[]> cVar = f67c;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }

    public static void b(Object obj, String str) {
        v1.c<String> cVar = f68d;
        if (cVar != null) {
            cVar.b(obj, str);
        }
    }

    public static void c(int i4, Object obj) {
        v1.c<Integer> cVar = f69e;
        if (cVar != null) {
            cVar.b(obj, Integer.valueOf(i4));
        }
    }
}
