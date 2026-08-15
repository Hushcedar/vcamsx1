package y0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class f extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public final int f1875m;

        static {
            perationCompat.init0(250);
        }

        public a() {
            super("android.media.session.ISessionManager");
            this.f1875m = -1;
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public class b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(244);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class c extends com.core.hack.handle.b {
        static {
            perationCompat.init0(243);
        }

        public c() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(603);
    }

    public f() {
        super("android.media.session.ISessionManager");
        d();
    }

    public static native boolean g(j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
