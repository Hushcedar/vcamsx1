package x1;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mDisabledChanges")
    private static v1.c<long[]> f1818a;

    static {
        j.e.q(e.class, "android.app.AppCompatCallbacks");
    }

    public static void a(Object obj, long[] jArr) {
        v1.c<long[]> cVar = f1818a;
        if (cVar != null) {
            cVar.b(obj, jArr);
        }
    }

    public static long[] b(Object obj) {
        v1.c<long[]> cVar = f1818a;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }
}
