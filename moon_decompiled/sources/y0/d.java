package y0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class d extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public int f1873m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public int f1874n;

        static {
            perationCompat.init0(365);
        }

        public a() {
            super("android.media.session.ISession");
            this.f1873m = -1;
            this.f1874n = -1;
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(605);
    }

    public d() {
        super("android.media.session.ISession");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
