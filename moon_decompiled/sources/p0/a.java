package p0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: p0.a$a, reason: collision with other inner class name */
    public class C0069a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(361);
        }

        public C0069a() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class b extends C0069a {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final Integer f1193i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final boolean f1194j;

        static {
            perationCompat.init0(356);
        }

        public b(Integer num) {
            super();
            this.f1193i = num;
            this.f1194j = false;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class c extends C0069a {
        static {
            perationCompat.init0(355);
        }

        public c() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(734);
    }

    public a() {
        super("android.hardware.location.IContextHubService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
