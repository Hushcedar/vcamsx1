package l1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    public class a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(704);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    /* JADX INFO: renamed from: l1.b$b, reason: collision with other inner class name */
    public class C0052b extends a {
        static {
            perationCompat.init0(705);
        }

        public C0052b() {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(412);
    }

    public b() {
        super("com.android.internal.telephony.IHwTelephony");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();

    public final native void g(Parcel parcel, String str);
}
