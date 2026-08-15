package k2;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "getCallingUserId", value = {})
    private static v1.h<Integer> f705b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "myUserId", value = {})
    private static v1.h<Integer> f706c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f704a = j.e.q(l.class, "android.os.UserHandle");

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static int f707d = -1;

    public static int a() {
        v1.h<Integer> hVar = f705b;
        if (hVar != null) {
            return hVar.a(null).intValue();
        }
        return 0;
    }

    public static int b() {
        if (f707d == -1) {
            v1.h<Integer> hVar = f706c;
            f707d = hVar != null ? hVar.a(null).intValue() : 0;
        }
        return f707d;
    }
}
