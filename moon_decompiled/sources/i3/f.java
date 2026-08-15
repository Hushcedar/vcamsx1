package i3;

import android.os.IBinder;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class f implements Parcelable, k {
    static {
        perationCompat.init0(125);
    }

    public abstract void b(d.a aVar, IBinder iBinder);

    public native void c();

    @Override // android.os.Parcelable
    public final native int describeContents();
}
