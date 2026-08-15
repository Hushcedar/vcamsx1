package f;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final a f340l;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f341a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public f f342b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public n3.d f343c;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public int f347g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public int f348h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public boolean f349i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public Map<String, Object> f350j;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Object f344d = new Object();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f345e = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final AtomicBoolean f346f = new AtomicBoolean(false);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final b f351k = new b();

    public class a extends k<e> {
        static {
            perationCompat.init0(593);
        }

        @Override // v3.k
        public final native e a();
    }

    public class b implements IBinder.DeathRecipient {
        static {
            perationCompat.init0(595);
        }

        public b() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public final native void binderDied();
    }

    static {
        perationCompat.init0(182);
        f340l = new a();
    }

    public static native Map d(n3.d dVar);

    public static native e e();

    public native boolean A();

    public native void B();

    public final native void a(n3.d dVar);

    public native void b(Context context);

    public native void c(Context context);

    public native Bundle f(Bundle bundle);

    public native int g();

    public native String h();

    public native String i();

    public native int j();

    public native c k();

    public native int l();

    public native Context m();

    public native String n();

    public native int o();

    public native int p();

    public native Map<String, Object> q();

    public native int r();

    public native void s(Context context);

    public native boolean t();

    public native boolean u();

    public native boolean v();

    public final native void w();

    public native boolean x();

    public native boolean y();

    public native void z(c cVar);
}
