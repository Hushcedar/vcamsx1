package f1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: f1.a$a, reason: collision with other inner class name */
    public class C0020a extends c {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Integer f358h = null;

        static {
            perationCompat.init0(312);
        }

        public C0020a() {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class b extends c {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Object f360h;

        static {
            perationCompat.init0(311);
        }

        public b(Object obj) {
            this.f360h = obj;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class c extends com.core.hack.handle.b {
        static {
            perationCompat.init0(310);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(749);
    }

    public a() {
        super("android.content.rollback.IRollbackManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
