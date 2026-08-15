package q;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class d extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final HashMap<String, com.core.hack.handle.a> f1240e;

    static {
        perationCompat.init0(451);
    }

    public d() {
        super("android.app.IActivityTaskManager");
        this.f1240e = new HashMap<>(2);
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
