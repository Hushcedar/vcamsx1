package u2;

import android.view.textclassifier.TextSelection;
import v1.e;
import v1.f;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "getSystemTextClassifierMetadata", value = {})
    private static f<Object> f1680a;

    static {
        j.e.r(b.class, TextSelection.Request.class);
    }

    public static Object a(TextSelection.Request request) {
        f<Object> fVar = f1680a;
        if (fVar != null) {
            return fVar.a(request, new Object[0]);
        }
        return null;
    }
}
