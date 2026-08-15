package v0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: v0.a$a, reason: collision with other inner class name */
    public static class C0091a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(730);
        }

        public C0091a() {
            super("android.content.pm.ILauncherApps");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(438);
    }

    public a() {
        super("android.content.pm.ILauncherApps");
    }

    @Override // com.core.hack.handle.c
    public final native b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
