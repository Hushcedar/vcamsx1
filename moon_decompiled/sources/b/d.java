package b;

import android.net.Uri;
import androidx.core.os.perationCompat;
import k3.p;
import r3.h;
import v3.i;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f20c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final h f21a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final p f22b;

    public class a extends k<d> {
        static {
            perationCompat.init0(315);
        }

        @Override // v3.k
        public final native d a();
    }

    static {
        perationCompat.init0(375);
        f20c = new a();
    }

    public d() {
        if (i.d()) {
            this.f21a = h.M3();
            this.f22b = p.S3();
        }
    }

    public static native d a();

    public native Uri b(Uri uri);
}
