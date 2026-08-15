package s1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public boolean f1473h = true;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f1474i = -1;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final a f1475j;

    static {
        perationCompat.init0(100);
    }

    public c(a aVar) {
        this.f1475j = aVar;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(j[] jVarArr, j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
