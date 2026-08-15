package r0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: r0.a$a, reason: collision with other inner class name */
    public class C0077a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(20);
        }

        public C0077a() {
            super("android.hardware.fingerprint.IFingerprintService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class b extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(17);
        }

        public b() {
            super("android.hardware.fingerprint.IFingerprintService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(116);
    }

    public a() {
        super("android.hardware.fingerprint.IFingerprintService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
