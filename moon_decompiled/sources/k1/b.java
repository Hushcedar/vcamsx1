package k1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends c {

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(268);
        }

        public a() {
            super("android.app.usage.IStorageStatsManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    /* JADX INFO: renamed from: k1.b$b, reason: collision with other inner class name */
    public static class C0046b extends com.core.hack.handle.b {
        static {
            perationCompat.init0(265);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(307);
    }

    public b() {
        super("android.app.usage.IStorageStatsManager");
    }

    public static native void g(j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
