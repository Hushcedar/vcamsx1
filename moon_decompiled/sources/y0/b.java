package y0;

import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(72);
        }

        public a() {
            super("android.hardware.ICameraService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(607);
    }

    public b() {
        super("android.hardware.ICameraService");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
