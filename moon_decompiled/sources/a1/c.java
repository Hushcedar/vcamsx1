package a1;

import androidx.core.os.perationCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static String f13e;

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(314);
        }

        public a() {
            super(c.f13e);
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public c() {
        super(f13e);
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();

    static {
        Field fieldA;
        perationCompat.init0(408);
        Object obj = null;
        try {
            fieldA = v3.j.a(Class.forName("vivo.app.security.IVivoPermissionService$Stub"), "DESCRIPTOR");
        } catch (ClassNotFoundException unused) {
            fieldA = null;
        }
        if (fieldA != null) {
            fieldA.setAccessible(true);
        }
        try {
            obj = fieldA.get(null);
        } catch (Exception unused2) {
        }
        f13e = (String) obj;
    }
}
