package a1;

import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {
    static {
        perationCompat.init0(428);
    }

    public b() {
        super("android.os.IStatsManagerService");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
