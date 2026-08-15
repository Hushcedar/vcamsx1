package n2;

import java.io.File;
import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "setupDiskCache", value = {File.class})
    private static h<Void> f1129a;

    static {
        j.e.q(a.class, "android.renderscript.RenderScriptCacheDir");
    }

    public static void a(File file) {
        h<Void> hVar = f1129a;
        if (hVar != null) {
            hVar.a(new Object[]{file});
        }
    }
}
