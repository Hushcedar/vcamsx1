package s1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class b extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final /* synthetic */ int f1468l = 0;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public int f1469h = -1;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f1470i = -1;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public int f1471j = -1;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final a f1472k;

    static {
        perationCompat.init0(93);
        int i4 = a.f1462d;
    }

    public b(a aVar) {
        this.f1472k = aVar;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(j[] jVarArr, j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
