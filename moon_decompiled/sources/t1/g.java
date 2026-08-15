package t1;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface g extends IInterface {

    public static abstract class a extends Binder implements g {

        /* JADX INFO: renamed from: t1.g$a$a, reason: collision with other inner class name */
        public static class C0085a implements g {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static g f1565b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f1566a;

            public C0085a(IBinder iBinder) {
                this.f1566a = iBinder;
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.f1566a;
            }

            @Override // t1.g
            public final void c1(int i4) {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IAssistCallback");
                    parcelObtain.writeInt(i4);
                    if (this.f1566a.transact(1, parcelObtain, null, 1) || a.F3() == null) {
                        return;
                    }
                    a.F3().c1(i4);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.core.hack.os.IAssistCallback");
        }

        public static g E3(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.core.hack.os.IAssistCallback");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof g)) ? new C0085a(iBinder) : (g) iInterfaceQueryLocalInterface;
        }

        public static g F3() {
            return C0085a.f1565b;
        }

        public static boolean G3(g gVar) {
            if (C0085a.f1565b != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (gVar == null) {
                return false;
            }
            C0085a.f1565b = gVar;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5) {
            if (i4 == 1598968902) {
                parcel2.writeString("com.core.hack.os.IAssistCallback");
                return true;
            }
            if (i4 != 1) {
                return super.onTransact(i4, parcel, parcel2, i5);
            }
            parcel.enforceInterface("com.core.hack.os.IAssistCallback");
            c1(parcel.readInt());
            return true;
        }
    }

    void c1(int i4);
}
