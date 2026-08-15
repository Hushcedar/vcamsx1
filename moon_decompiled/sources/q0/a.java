package q0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: q0.a$a, reason: collision with other inner class name */
    public static class C0073a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(609);
        }

        public C0073a() {
            super("android.hardware.display.IDisplayManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(11);
    }

    public a() {
        super("android.hardware.display.IDisplayManager");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
