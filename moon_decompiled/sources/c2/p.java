package c2;

/* JADX INFO: loaded from: classes.dex */
public final class p {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "", value = {int.class, String.class, int.class})
    private static v1.a<Object> f224a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "FLAG_PRIMARY")
    private static v1.g<Integer> f225b;

    static {
        j.e.q(p.class, "android.content.pm.UserInfo");
    }

    public static int a() {
        v1.g<Integer> gVar = f225b;
        if (gVar != null) {
            return gVar.a().intValue();
        }
        return 0;
    }

    public static Object b(int i4) {
        v1.a<Object> aVar = f224a;
        if (aVar != null) {
            return aVar.a(new Object[]{0, "user", Integer.valueOf(i4)});
        }
        return null;
    }
}
