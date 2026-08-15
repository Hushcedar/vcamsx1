package l1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class d extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final /* synthetic */ int f1025e = 0;

    public static class a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public boolean f1026h = true;

        static {
            perationCompat.init0(640);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public boolean f1027h = true;

        static {
            perationCompat.init0(641);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class c extends a {
        static {
            perationCompat.init0(642);
        }

        public c(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    /* JADX INFO: renamed from: l1.d$d, reason: collision with other inner class name */
    public static class C0054d extends a {
        static {
            perationCompat.init0(644);
        }

        public C0054d(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class e extends a {
        static {
            perationCompat.init0(645);
        }

        public e(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(417);
    }

    public d() {
        super("com.android.internal.telephony.IPhoneSubInfo");
        d();
    }

    public static native boolean g(Class[] clsArr, Object[] objArr);

    public static native boolean h(j[] jVarArr, Class[] clsArr);

    public static native void i(Parcel parcel, String str);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
