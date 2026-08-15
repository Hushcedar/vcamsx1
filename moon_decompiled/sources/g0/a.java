package g0;

import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: g0.a$a, reason: collision with other inner class name */
    public static class C0022a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(12);
        }

        public C0022a() {
            super("android.app.IWallpaperManager");
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(74);
    }

    public a() {
        super("android.app.IWallpaperManager");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
