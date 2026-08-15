package b1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: b1.a$a, reason: collision with other inner class name */
    public class C0004a extends b {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final Integer f54i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final boolean f55j;

        static {
            perationCompat.init0(163);
        }

        public C0004a(Integer num) {
            super();
            this.f54i = num;
            this.f55j = false;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(165);
        }

        public b() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class c extends b {
        static {
            perationCompat.init0(169);
        }

        public c() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(456);
    }

    public a() {
        super("android.service.persistentdata.IPersistentDataBlockService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
