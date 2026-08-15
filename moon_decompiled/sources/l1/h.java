package l1;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class h extends com.core.hack.handle.c {

    public static class a extends f {
        static {
            perationCompat.init0(69);
        }

        @Override // l1.f, com.core.hack.handle.b
        public final native String c();
    }

    static {
        perationCompat.init0(432);
    }

    public h() {
        super("com.android.internal.telephony.ITelephonyRegistry");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
