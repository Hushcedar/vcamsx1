package x3;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface b extends IInterface {

    public static abstract class a extends Binder implements b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f1863a = 0;

        /* JADX INFO: renamed from: x3.b$a$a, reason: collision with other inner class name */
        public static class C0099a implements b {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f1864a;

            public C0099a(IBinder iBinder) {
                this.f1864a = iBinder;
            }

            @Override // x3.b
            public final w3.a H(int i4) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("waxmoon.ipc.request");
                    parcelObtain.writeInt(i4);
                    if (!this.f1864a.transact(2, parcelObtain, parcelObtain2, 0)) {
                        return null;
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (w3.a) parcelObtain2.readParcelable(b.class.getClassLoader()) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // x3.b
            public final int W() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("waxmoon.ipc.request");
                    if (!this.f1864a.transact(1, parcelObtain, parcelObtain2, 0)) {
                        return 0;
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.f1864a;
            }

            @Override // x3.b
            public final void close() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("waxmoon.ipc.request");
                    if (this.f1864a.transact(3, parcelObtain, parcelObtain2, 0)) {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "waxmoon.ipc.request");
        }

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5) {
            if (i4 == 1) {
                parcel.enforceInterface("waxmoon.ipc.request");
                int iW = ((e) this).W();
                parcel2.writeNoException();
                parcel2.writeInt(iW);
                return true;
            }
            if (i4 != 2) {
                if (i4 != 3) {
                    if (i4 != 1598968902) {
                        return super.onTransact(i4, parcel, parcel2, i5);
                    }
                    parcel2.writeString("waxmoon.ipc.request");
                    return true;
                }
                parcel.enforceInterface("waxmoon.ipc.request");
                ((x3.a) this).close();
                parcel2.writeNoException();
                return true;
            }
            parcel.enforceInterface("waxmoon.ipc.request");
            w3.a aVarH = ((e) this).H(parcel.readInt());
            parcel2.writeNoException();
            if (aVarH != null) {
                parcel2.writeInt(1);
                parcel2.writeParcelable(aVarH, 1);
            } else {
                parcel2.writeInt(0);
            }
            return true;
        }
    }

    w3.a H(int i4);

    int W();

    void close();
}
