package m;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final boolean f1035e = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final String f1036f = "a";

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final boolean f1037g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final String f1038h = "android.content.IOplusClipboardManager";

    /* JADX INFO: renamed from: m.a$a, reason: collision with other inner class name */
    public static class C0056a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(415);
        }

        public C0056a() {
            super(a.f1038h);
        }

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(637);
    }

    public a() {
        super(f1038h);
    }

    @Override // com.core.hack.handle.c
    public native b b(Method method);

    @Override // com.core.hack.handle.c
    public native HashMap<String, b> c();
}
