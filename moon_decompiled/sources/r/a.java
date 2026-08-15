package r;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: r.a$a, reason: collision with other inner class name */
    public static class C0076a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(685);
        }

        public C0076a() {
            super("com.android.internal.app.IAppOpsService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c
        public final native boolean n(Class[] clsArr, Object[] objArr);
    }

    public class b extends C0076a {
        static {
            perationCompat.init0(690);
        }

        public b() {
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public class c extends C0076a {
        static {
            perationCompat.init0(688);
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(381);
    }

    public a() {
        super("com.android.internal.app.IAppOpsService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
