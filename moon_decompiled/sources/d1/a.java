package d1;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: d1.a$a, reason: collision with other inner class name */
    public class C0013a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(276);
        }

        public C0013a() {
            super("android.content.IRestrictionsManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(635);
    }

    public a() {
        super("android.content.IRestrictionsManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
