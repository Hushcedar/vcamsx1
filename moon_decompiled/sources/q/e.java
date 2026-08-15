package q;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class e extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(591);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);
    }

    static {
        perationCompat.init0(450);
    }

    public e() {
        super("android.app.IAppTask");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
