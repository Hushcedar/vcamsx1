package q;

import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class p0 extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1273h;

    static {
        perationCompat.init0(32);
    }

    public p0(String str) {
        this.f1273h = str;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
