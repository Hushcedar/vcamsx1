package q;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class i extends com.core.hack.handle.c {

    public static class a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public int f1247h = -1;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public int f1248i = -1;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public int f1249j = -1;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public int f1250k = -1;

        static {
            perationCompat.init0(382);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(454);
    }

    public i() {
        super("android.content.IIntentSender");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
