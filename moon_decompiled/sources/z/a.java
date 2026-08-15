package z;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.os.perationCompat;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a extends Instrumentation {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final C0101a f1895d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Object f1896a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Instrumentation f1897b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f1898c;

    /* JADX INFO: renamed from: z.a$a, reason: collision with other inner class name */
    public class C0101a extends k<a> {
        static {
            perationCompat.init0(589);
        }

        @Override // v3.k
        public final native a a();
    }

    static {
        perationCompat.init0(19);
        f1895d = new C0101a();
    }

    public static native a a();

    public final native void b(Activity activity, Bundle bundle, PersistableBundle persistableBundle);

    public final native synchronized void c();

    @Override // android.app.Instrumentation
    public final native void callActivityOnCreate(Activity activity, Bundle bundle);

    @Override // android.app.Instrumentation
    public final native void callActivityOnCreate(Activity activity, Bundle bundle, PersistableBundle persistableBundle);
}
