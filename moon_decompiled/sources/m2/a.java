package m2;

import android.content.Context;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "setApplicationContextForResources", value = {Context.class})
    private static h<Void> f1040a;

    static {
        j.e.q(a.class, "android.provider.FontsContract");
    }

    public static void a(Context context) {
        h<Void> hVar = f1040a;
        if (hVar != null) {
            hVar.a(new Object[]{context});
        }
    }
}
