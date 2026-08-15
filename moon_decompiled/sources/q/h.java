package q;

import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class h extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1244h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public volatile Object f1245i;

    static {
        perationCompat.init0(455);
    }

    public h(String str) {
        this.f1244h = str;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
