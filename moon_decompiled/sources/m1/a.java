package m1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: m1.a$a, reason: collision with other inner class name */
    public class C0058a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(292);
        }

        public C0058a() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class b extends C0058a {
        static {
            perationCompat.init0(300);
        }

        public b(a aVar) {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(447);
    }

    public a() {
        super("android.net.ITetheringConnector");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
