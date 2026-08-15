package a0;

import android.app.PendingIntent;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: a0.a$a, reason: collision with other inner class name */
    public static class C0000a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(584);
        }

        public C0000a() {
            super("android.net.IConnectivityManager");
        }

        @Override // com.core.hack.handle.b
        public native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends C0000a {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public int f8m = -1;

        static {
            perationCompat.init0(583);
        }

        @Override // a0.a.C0000a, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    public static class c extends C0000a {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public int f9m = -1;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public final ThreadLocal<PendingIntent> f10n = new ThreadLocal<>();

        static {
            perationCompat.init0(519);
        }

        @Override // a0.a.C0000a, com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(509);
    }

    public a() {
        super("android.net.IConnectivityManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
