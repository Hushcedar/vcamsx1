package y0;

import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class e extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(322);
        }

        public a() {
            super("android.media.IMediaRouterService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(604);
    }

    public e() {
        super("android.media.IMediaRouterService");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
