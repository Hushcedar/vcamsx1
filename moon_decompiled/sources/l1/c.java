package l1;

import androidx.core.os.perationCompat;
import java.io.Serializable;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.b {
        static {
            perationCompat.init0(665);
        }

        public a(Serializable serializable) {
            super(serializable, "com.android.internal.telephony.ISub");
        }

        @Override // com.core.hack.handle.client.b
        public final native boolean m();
    }

    public static class b extends f {
        static {
            perationCompat.init0(666);
        }

        @Override // l1.f, com.core.hack.handle.b
        public final native String c();
    }

    /* JADX INFO: renamed from: l1.c$c, reason: collision with other inner class name */
    public static class C0053c extends a {
        static {
            perationCompat.init0(668);
        }

        public C0053c(Object obj) {
            super((Serializable) obj);
        }

        @Override // com.core.hack.handle.client.b, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class d extends b {
        static {
            perationCompat.init0(669);
        }

        public d(int i4) {
        }

        @Override // l1.f, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(410);
    }

    public c() {
        super("com.android.internal.telephony.ISub");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
