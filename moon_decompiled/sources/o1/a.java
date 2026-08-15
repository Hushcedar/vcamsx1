package o1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: o1.a$a, reason: collision with other inner class name */
    public class C0067a extends b {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final Integer f1151i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final boolean f1152j;

        static {
            perationCompat.init0(564);
        }

        public C0067a(Integer num) {
            super();
            this.f1151i = num;
            this.f1152j = false;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(561);
        }

        public b() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class c extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(559);
        }

        public c() {
            super("android.os.IUserManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class d extends b {
        static {
            perationCompat.init0(568);
        }

        public d() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class e extends b {
        static {
            perationCompat.init0(567);
        }

        public e() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class f extends b {
        static {
            perationCompat.init0(566);
        }

        public f() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class g extends b {
        static {
            perationCompat.init0(565);
        }

        public g(a aVar) {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class h extends b {
        static {
            perationCompat.init0(569);
        }

        public h() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(588);
    }

    public a() {
        super("android.os.IUserManager");
        d();
    }

    public static native void h(j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
