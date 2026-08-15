package q;

import android.os.Build;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final HashMap<String, String> f1230f;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final HashMap<String, com.core.hack.handle.a> f1231e;

    public class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(67);
        }

        public a() {
            super("android.app.IActivityManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    /* JADX INFO: renamed from: q.b$b, reason: collision with other inner class name */
    public static class C0072b extends d {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Integer f1232h;

        static {
            perationCompat.init0(65);
        }

        public C0072b(Integer num) {
            this.f1232h = num;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class c extends com.core.hack.handle.b {
        static {
            perationCompat.init0(62);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class d extends com.core.hack.handle.b {
        static {
            perationCompat.init0(60);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    public static class e extends d {
        static {
            perationCompat.init0(58);
        }

        public e(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class f extends k {
        public f(int i4) {
            super(0);
        }
    }

    public class g extends d {
        static {
            perationCompat.init0(57);
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class h extends d {
        static {
            perationCompat.init0(56);
        }

        public h() {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class i extends com.core.hack.handle.b {
        static {
            perationCompat.init0(55);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class j extends d {
        static {
            perationCompat.init0(54);
        }

        public j(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class k extends d {
        static {
            perationCompat.init0(51);
        }

        public k(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class l extends k {
        public l(int i4) {
            super(0);
        }
    }

    public class m extends com.core.hack.handle.b {
        static {
            perationCompat.init0(49);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class n extends d {
        static {
            perationCompat.init0(48);
        }

        public n(int i4) {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(449);
        if (Build.VERSION.SDK_INT < 26) {
            HashMap<String, String> map = new HashMap<>(1);
            f1230f = map;
            map.put("getDeviceConfigurationInfo", "getDeviceConfiguration");
        }
    }

    public b() {
        super("android.app.IActivityManager");
        this.f1231e = new HashMap<>(2);
        d();
    }

    @Override // com.core.hack.handle.c
    public final native String a(String str);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
