package b2;

import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "asInterface", value = {IBinder.class})
    private static v1.h<IInterface> f72a;

    static {
        j.e.q(e.class, "android.content.ContentProviderNative");
    }

    public static IInterface a(IBinder iBinder) {
        v1.h<IInterface> hVar = f72a;
        if (hVar != null) {
            return hVar.a(new Object[]{iBinder});
        }
        return null;
    }
}
