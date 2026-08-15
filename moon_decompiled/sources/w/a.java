package w;

import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: w.a$a, reason: collision with other inner class name */
    public static class C0092a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(367);
        }

        public C0092a() {
            super("android.os.IDeviceIdleController");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(150);
    }

    public a() {
        super("android.os.IDeviceIdleController");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
