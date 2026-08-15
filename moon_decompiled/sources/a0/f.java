package a0;

import android.annotation.TargetApi;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class f extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(342);
        }

        public a() {
            super("android.net.wifi.IWifiManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public boolean f11m;

        static {
            perationCompat.init0(350);
        }

        public b() {
            super("android.net.wifi.IWifiManager");
            this.f11m = true;
        }

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    @TargetApi(30)
    public static class c extends b {
        static {
            perationCompat.init0(346);
        }

        public c(int i4) {
        }

        @Override // a0.f.b, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    static {
        perationCompat.init0(505);
    }

    public f() {
        super("android.net.wifi.IWifiManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
