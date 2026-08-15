package l1;

import android.annotation.TargetApi;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(29)
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: l1.a$a, reason: collision with other inner class name */
    public static class C0051a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(377);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(404);
    }

    public a() {
        super("android.os.IDeviceIdentifiersPolicyService");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
