package c2;

import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "asInterface", value = {IBinder.class})
    private static v1.h<IInterface> f105a;

    static {
        j.e.q(c.class, "android.content.pm.IPackageManager$Stub");
    }

    public static IInterface a(IBinder iBinder) {
        v1.h<IInterface> hVar = f105a;
        if (hVar != null) {
            return hVar.a(new Object[]{iBinder});
        }
        return null;
    }
}
