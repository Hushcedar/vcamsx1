package g2;

import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "setTargetSdkVersion", value = {int.class})
    private static h<Void> f381a;

    static {
        j.e.q(b.class, "android.graphics.Compatibility");
    }

    public static void a(int i4) {
        h<Void> hVar = f381a;
        if (hVar != null) {
            hVar.a(new Object[]{Integer.valueOf(i4)});
        }
    }
}
