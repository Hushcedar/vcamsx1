package h2;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import j.e;
import v1.b;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(23)
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @b(name = "mType")
    private static c<Integer> f387a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @b(name = "mString1")
    private static c<String> f388b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @b(name = "mObj1")
    private static c<Object> f389c;

    static {
        e.r(a.class, Icon.class);
    }

    public static void a(Icon icon, Object obj) {
        c<Object> cVar = f389c;
        if (cVar != null) {
            cVar.b(icon, obj);
        }
    }

    public static String b(Icon icon) {
        c<String> cVar = f388b;
        if (cVar != null) {
            return cVar.a(icon);
        }
        return null;
    }

    public static void c(Icon icon, String str) {
        c<String> cVar = f388b;
        if (cVar != null) {
            cVar.b(icon, str);
        }
    }

    public static int d(Icon icon) {
        c<Integer> cVar = f387a;
        if (cVar != null) {
            return cVar.a(icon).intValue();
        }
        return -1;
    }

    public static void e(Icon icon) {
        c<Integer> cVar = f387a;
        if (cVar != null) {
            cVar.b(icon, 1);
        }
    }
}
