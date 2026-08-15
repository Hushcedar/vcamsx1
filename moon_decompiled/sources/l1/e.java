package l1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class e extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public boolean f1028h = true;

        static {
            perationCompat.init0(592);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

        @Override // com.core.hack.handle.b
        public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class b extends a {
        static {
            perationCompat.init0(594);
        }

        public b(int i4) {
        }

        @Override // l1.e.a, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // l1.e.a, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class c extends com.core.hack.handle.b {
        static {
            perationCompat.init0(596);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(414);
    }

    public e() {
        super("com.android.internal.telecom.ITelecomService");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
