package c0;

import android.content.pm.ApplicationInfo;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f82a;

    public class a extends v3.k<d> {
        static {
            perationCompat.init0(113);
        }

        @Override // v3.k
        public final native d a();
    }

    static {
        perationCompat.init0(633);
        f82a = new a();
    }

    public static native d a();

    public static native boolean b(ApplicationInfo applicationInfo);

    public static native boolean c(String str);
}
