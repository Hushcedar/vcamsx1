package c0;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: c0.a$a, reason: collision with other inner class name */
    public static class C0006a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(76);
        }

        public C0006a() {
            super("android.content.pm.ICrossProfileApps");
        }

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);
    }

    static {
        perationCompat.init0(573);
    }

    public a() {
        super("android.content.pm.ICrossProfileApps");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
