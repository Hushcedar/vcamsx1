package u;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    public static class a extends u.a {
        static {
            perationCompat.init0(520);
        }

        public a() {
            super("android.bluetooth.IBluetooth");
        }

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);
    }

    static {
        perationCompat.init0(228);
    }

    public b() {
        super("android.bluetooth.IBluetooth");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
