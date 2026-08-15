package n0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final /* synthetic */ int f1127e = 0;

    public static class a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final boolean f1128h = true;

        static {
            perationCompat.init0(168);
            int i4 = b.f1127e;
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(621);
    }

    public b() {
        super("android.sec.clipboard.IClipboardService");
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
