package j1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: j1.a$a, reason: collision with other inner class name */
    public static class C0041a extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public final int f548m;

        static {
            perationCompat.init0(90);
        }

        public C0041a(int i4) {
            super("com.android.internal.telephony.ISms");
            this.f548m = -1;
            this.f548m = i4;
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(267);
    }

    public a() {
        super("com.android.internal.telephony.ISms");
    }

    @Override // com.core.hack.handle.c
    public final native b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
