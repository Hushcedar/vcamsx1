package t1;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import q.t;

/* JADX INFO: loaded from: classes.dex */
public interface h extends IInterface {

    public static abstract class a extends Binder implements h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f1567a = 0;

        /* JADX INFO: renamed from: t1.h$a$a, reason: collision with other inner class name */
        public static class C0086a implements h {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f1568a;

            public C0086a(IBinder iBinder) {
                this.f1568a = iBinder;
            }

            @Override // t1.h
            public final void a1(int i4, Intent intent, Bundle bundle, String str) {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IVIntentReceiver");
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(i4);
                    parcelObtain.writeString(str);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.f1568a.transact(1, parcelObtain, null, 1)) {
                        int i5 = a.f1567a;
                    }
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.f1568a;
            }
        }

        public a() {
            attachInterface(this, "com.core.hack.os.IVIntentReceiver");
        }

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5) {
            if (i4 == 1598968902) {
                parcel2.writeString("com.core.hack.os.IVIntentReceiver");
                return true;
            }
            if (i4 != 1) {
                return super.onTransact(i4, parcel, parcel2, i5);
            }
            parcel.enforceInterface("com.core.hack.os.IVIntentReceiver");
            Intent intent = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
            ((t) this).a1(parcel.readInt(), intent, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readString());
            return true;
        }
    }

    void a1(int i4, Intent intent, Bundle bundle, String str);
}
