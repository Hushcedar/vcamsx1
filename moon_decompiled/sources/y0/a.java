package y0;

import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: y0.a$a, reason: collision with other inner class name */
    public static class C0100a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(112);
        }

        public C0100a() {
            super("android.media.IAudioService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(608);
    }

    public a() {
        super("android.media.IAudioService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
