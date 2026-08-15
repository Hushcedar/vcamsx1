package u;

import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class d extends com.core.hack.handle.c {
    static {
        perationCompat.init0(231);
    }

    public d() {
        super("android.bluetooth.IBluetoothManager");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
