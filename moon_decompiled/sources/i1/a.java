package i1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: i1.a$a, reason: collision with other inner class name */
    public class C0039a extends b {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final Integer f437i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final boolean f438j;

        static {
            perationCompat.init0(560);
        }

        public C0039a(Integer num) {
            super();
            this.f437i = num;
            this.f438j = false;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(570);
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
            perationCompat.init0(571);
        }

        public c() {
            super("android.app.slice.ISliceManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(151);
    }

    public a() {
        super("android.app.slice.ISliceManager");
        d();
    }

    public static native void h(j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
