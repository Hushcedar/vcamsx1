package l;

import android.app.Application;
import androidx.core.os.perationCompat;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static k<a> f1012f;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Application f1013a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f1014b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f1015c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f1016d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f1017e = false;

    /* JADX INFO: renamed from: l.a$a, reason: collision with other inner class name */
    public class C0049a extends k<a> {
        static {
            perationCompat.init0(550);
        }

        @Override // v3.k
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public native a a();
    }

    static {
        perationCompat.init0(664);
        f1012f = new C0049a();
    }

    public static native a a();

    public static native boolean b();

    public static native boolean c();

    public static native boolean d();

    public static native boolean e();
}
