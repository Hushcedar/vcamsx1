package s0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: s0.a$a, reason: collision with other inner class name */
    public static final class C0080a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(600);
        }

        public C0080a() {
            super("android.view.IGraphicsStats");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(164);
    }

    public a() {
        super("android.view.IGraphicsStats");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
