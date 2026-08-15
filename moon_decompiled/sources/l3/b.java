package l3;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class b extends Binder implements IInterface {
    static {
        perationCompat.init0(334);
    }

    public b() {
        try {
            attachInterface(this, "android.os.IUpdateEngine");
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    @Override // android.os.IInterface
    public final native IBinder asBinder();

    @Override // android.os.Binder
    public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
}
