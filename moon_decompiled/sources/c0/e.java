package c0;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class e extends com.core.hack.handle.b {
    static {
        perationCompat.init0(632);
    }

    public static native long m(Object obj);

    public static native void p(Parcel parcel, String str);

    public static native void q(Parcel parcel, List list, boolean z3);

    @Override // com.core.hack.handle.b
    public native String c();

    @Override // com.core.hack.handle.b
    public native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

    public final native boolean n(Parcel parcel, Integer num);

    public final native boolean o(Parcel parcel, Parcelable parcelable);
}
