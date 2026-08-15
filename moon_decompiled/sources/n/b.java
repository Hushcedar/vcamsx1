package n;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final boolean f1118e = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final String f1119f = "b";

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final boolean f1120g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final String f1121h = "com.android.internal.graphics.fonts.IFontManager";

    /* JADX INFO: renamed from: n.b$b, reason: collision with other inner class name */
    public static class C0062b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(248);
        }

        private C0062b() {
        }

        @Override // com.core.hack.handle.b
        public native String c();

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class c extends com.core.hack.handle.b {
        static {
            perationCompat.init0(326);
        }

        private c() {
        }

        @Override // com.core.hack.handle.b
        public native String c();

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(585);
    }

    public b() {
        super(f1121h);
    }

    @Override // com.core.hack.handle.c
    public native HashMap<String, com.core.hack.handle.b> c();
}
