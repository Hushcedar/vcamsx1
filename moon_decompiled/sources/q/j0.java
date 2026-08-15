package q;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class j0 extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1257h = "android.app.IActivityManager";

    static {
        perationCompat.init0(647);
        HashMap<String, String> map = b.f1230f;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public final native void h(u1.j[] jVarArr, u1.j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);
}
