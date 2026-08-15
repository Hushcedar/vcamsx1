package t0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: t0.a$a, reason: collision with other inner class name */
    public class C0083a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(405);
        }

        public C0083a() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    public class b extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(394);
        }

        public b() {
            super("android.content.pm.IPackageInstaller");
        }

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    public class c extends C0083a {
        static {
            perationCompat.init0(391);
        }

        public c() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class d extends b {
        static {
            perationCompat.init0(398);
        }

        public d() {
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public class e extends C0083a {
        static {
            perationCompat.init0(396);
        }

        public e() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class f extends b {
        static {
            perationCompat.init0(441);
        }

        public f() {
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public class g extends C0083a {
        static {
            perationCompat.init0(440);
        }

        public g() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class h extends C0083a {
        static {
            perationCompat.init0(443);
        }

        public h() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class i extends C0083a {
        static {
            perationCompat.init0(442);
        }

        public i() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class j extends C0083a {
        static {
            perationCompat.init0(436);
        }

        public j() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class k extends C0083a {
        static {
            perationCompat.init0(435);
        }

        public k() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class l extends C0083a {
        static {
            perationCompat.init0(439);
        }

        public l() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class m extends C0083a {
        static {
            perationCompat.init0(437);
        }

        public m() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(295);
    }

    public a() {
        super("android.content.pm.IPackageInstaller");
        d();
    }

    public static native r3.k h();

    public static native void i(u1.j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();

    public final native boolean j(Parcel parcel, Integer num);
}
