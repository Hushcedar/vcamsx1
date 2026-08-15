package m2;

import android.content.ContentResolver;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getString", value = {})
    private static v1.f<String> f1042a;

    static {
        j.e.q(c.class, "android.provider.Settings$Config");
    }

    public static Object a(ContentResolver contentResolver, String str) {
        v1.f<String> fVar = f1042a;
        if (fVar != null) {
            return fVar.a(contentResolver, new Object[]{str});
        }
        return null;
    }
}
