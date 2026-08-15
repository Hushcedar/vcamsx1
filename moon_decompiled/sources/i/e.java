package i;

import android.net.Uri;
import android.os.IBinder;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f404b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public n f405a;

    public class a extends v3.k<e> {
        static {
            perationCompat.init0(167);
        }

        @Override // v3.k
        public final native e a();
    }

    static {
        perationCompat.init0(43);
        f404b = new a();
    }

    public static native e a();

    public native void b();

    public native void c(Uri uri, IBinder iBinder, boolean z3, int i4, int i5, int i6);

    public native void d(Uri uri, boolean z3, IBinder iBinder, int i4, int i5);

    public native void e(IBinder iBinder);
}
