package v;

import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public class g extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final boolean f1705h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f1706i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final String f1707j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final boolean f1708k;

    static {
        perationCompat.init0(194);
    }

    public g() {
        this.f1705h = true;
        this.f1706i = -1;
        this.f1708k = false;
        this.f1707j = "android.content.IContentProvider";
    }

    public g(int i4) {
        this.f1705h = true;
        this.f1706i = -1;
        this.f1708k = false;
        this.f1707j = "android.content.IContentProvider";
        this.f1708k = true;
    }

    public static native void m(Object obj, String str);

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

    public final native boolean n(Object[] objArr);

    public final native boolean o(Object[] objArr);
}
