package b;

import android.content.Context;
import android.content.Intent;
import androidx.core.os.perationCompat;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final C0002a f19a;

        /* JADX INFO: renamed from: b.b$a$a, reason: collision with other inner class name */
        public class C0002a extends k<a> {
            static {
                perationCompat.init0(83);
            }

            @Override // v3.k
            public final native a a();
        }

        static {
            perationCompat.init0(15);
            f19a = new C0002a();
        }

        public static native a a();
    }

    static {
        perationCompat.init0(373);
    }

    public static native void a(Context context, Intent intent);
}
