package a0;

import android.net.wifi.rtt.WifiRttManager;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class i extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static String f12e;

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(99);
        }

        public a() {
            super(i.f12e);
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends a {
        static {
            perationCompat.init0(95);
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public i() {
        super(f12e);
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();

    static {
        perationCompat.init0(499);
        WifiRttManager wifiRttManager = (WifiRttManager) f.e.e().m().getSystemService("wifirtt");
        if (wifiRttManager != null) {
            try {
                f12e = j2.a.a(wifiRttManager).asBinder().getInterfaceDescriptor();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }
}
