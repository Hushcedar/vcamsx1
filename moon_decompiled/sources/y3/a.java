package y3;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import k3.b;

/* JADX INFO: loaded from: classes.dex */
public abstract class a extends Binder implements IInterface {
    public a() {
        attachInterface(this, "com.core.hack.view.IApplicationToken");
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public final boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5) {
        if (i4 == 1598968902) {
            parcel2.writeString("com.core.hack.view.IApplicationToken");
            return true;
        }
        if (i4 != 1) {
            return super.onTransact(i4, parcel, parcel2, i5);
        }
        parcel.enforceInterface("com.core.hack.view.IApplicationToken");
        parcel2.writeNoException();
        parcel2.writeString(((b.a) this).f761b);
        return true;
    }
}
