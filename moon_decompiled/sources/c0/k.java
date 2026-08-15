package c0;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class k extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(421);
        }

        public a() {
            super("android.app.IUriGrantsManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    static {
        perationCompat.init0(611);
    }

    public k() {
        super("android.app.IUriGrantsManager");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
