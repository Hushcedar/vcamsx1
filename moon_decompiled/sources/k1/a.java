package k1;

import android.os.Build;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final String f680e;

    /* JADX INFO: renamed from: k1.a$a, reason: collision with other inner class name */
    public static class C0045a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final String f681h;

        static {
            perationCompat.init0(206);
        }

        public C0045a(String str) {
            this.f681h = str;
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
        public final String f682h;

        static {
            perationCompat.init0(205);
        }

        public b(String str) {
            this.f682h = str;
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(308);
    }

    public a(String str) {
        super(Build.VERSION.SDK_INT < 26 ? "android.os.storage.IMountService" : str);
        this.f680e = str;
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
