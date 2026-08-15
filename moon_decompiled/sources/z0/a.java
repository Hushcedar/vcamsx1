package z0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: z0.a$a, reason: collision with other inner class name */
    public static class C0102a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(122);
        }

        public C0102a() {
            super("com.android.internal.telephony.IMms");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(695);
    }

    public a() {
        super("com.android.internal.telephony.IMms");
    }

    @Override // com.core.hack.handle.c
    public final native b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
