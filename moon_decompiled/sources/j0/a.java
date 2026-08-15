package j0;

import android.app.PendingIntent;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: j0.a$a, reason: collision with other inner class name */
    public static class C0040a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(708);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class b extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public int f544h = -1;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final ThreadLocal<PendingIntent> f545i = new ThreadLocal<>();

        static {
            perationCompat.init0(709);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class c extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public int f546m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public int f547n;

        static {
            perationCompat.init0(710);
        }

        public c() {
            super("android.app.IAlarmManager");
            this.f546m = -1;
            this.f547n = -1;
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(330);
    }

    public a() {
        super("android.app.IAlarmManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
