package a1;

import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: a1.a$a, reason: collision with other inner class name */
    public static class C0001a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(512);
        }

        public C0001a() {
            super("meizu.security.IFlymePermissionService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(424);
    }

    public a() {
        super("meizu.security.IFlymePermissionService");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
