package n0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import l1.d;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: n0.a$a, reason: collision with other inner class name */
    public static class C0063a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final boolean f1126h = true;

        static {
            perationCompat.init0(114);
            int i4 = d.f1025e;
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(618);
    }

    public a() {
        super("android.content.IClipboard");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
