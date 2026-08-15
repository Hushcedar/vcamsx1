package s1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class d extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final a f1476h;

    static {
        perationCompat.init0(98);
        int i4 = a.f1462d;
    }

    public d(a aVar) {
        this.f1476h = aVar;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(j[] jVarArr, j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
