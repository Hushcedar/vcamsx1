package u2;

import j.e;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mCallingPackageName")
    private static c<String> f1679a;

    static {
        e.q(a.class, "android.view.textclassifier.SystemTextClassifierMetadata");
    }

    public static void a(Object obj, String str) {
        c<String> cVar = f1679a;
        if (cVar != null) {
            cVar.b(obj, str);
        }
    }
}
