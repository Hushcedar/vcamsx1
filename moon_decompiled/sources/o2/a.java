package o2;

import android.content.Context;
import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "install", value = {Context.class})
    private static h<Void> f1159a;

    static {
        j.e.q(a.class, "android.security.net.config.NetworkSecurityConfigProvider");
    }

    public static void a(Context context) {
        h<Void> hVar = f1159a;
        if (hVar != null) {
            hVar.a(new Object[]{context});
        }
    }
}
