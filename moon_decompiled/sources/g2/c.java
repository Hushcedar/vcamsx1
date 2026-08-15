package g2;

import java.io.File;
import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "setupDiskCache", value = {File.class})
    private static h<Void> f382a;

    static {
        j.e.q(c.class, "android.graphics.HardwareRenderer");
    }

    public static void a(File file) {
        h<Void> hVar = f382a;
        if (hVar != null) {
            hVar.a(new Object[]{file});
        }
    }
}
