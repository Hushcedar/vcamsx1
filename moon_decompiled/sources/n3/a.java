package n3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0065a f1130b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1131a;

    /* JADX INFO: renamed from: n3.a$a, reason: collision with other inner class name */
    public class C0065a extends k<a> {
        static {
            perationCompat.init0(713);
        }

        @Override // v3.k
        public final native a a();
    }

    static {
        perationCompat.init0(500);
        f1130b = new C0065a();
    }

    public static native a a();

    public native Object b(Activity activity);

    public native Object c(Activity activity);

    public native Object d(IBinder iBinder, Intent intent);

    public native Bundle e(String str, Bundle bundle);

    public native void f(Context context);

    public native Bundle g(String str, String str2, Bundle bundle);
}
