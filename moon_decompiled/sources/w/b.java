package w;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import u1.g;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final /* synthetic */ int f1739e = 0;

    public static class a extends c {
        public a() {
        }

        public a(ArrayList arrayList) {
            this.f1740h = arrayList;
        }
    }

    /* JADX INFO: renamed from: w.b$b, reason: collision with other inner class name */
    public static class C0093b extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(335);
        }

        public C0093b() {
            super("android.app.admin.IDevicePolicyManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class c extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public Object f1740h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public g f1741i;

        static {
            perationCompat.init0(333);
            int i4 = b.f1739e;
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(152);
    }

    public b() {
        super("android.app.admin.IDevicePolicyManager");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
