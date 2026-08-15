package t1;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import k3.s;
import t1.h;

/* JADX INFO: loaded from: classes.dex */
public interface i extends IInterface {

    public static abstract class a extends Binder implements i {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f1569a = 0;

        /* JADX INFO: renamed from: t1.i$a$a, reason: collision with other inner class name */
        public static class C0087a implements i {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f1570a;

            public C0087a(IBinder iBinder) {
                this.f1570a = iBinder;
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.f1570a;
            }

            @Override // t1.i
            public final void f1(Intent intent) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IVIntentSender");
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.f1570a.transact(2, parcelObtain, parcelObtain2, 0)) {
                        int i4 = a.f1569a;
                    }
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // t1.i
            public final int h(int i4, Intent intent, String str, h hVar, String str2) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IVIntentSender");
                    parcelObtain.writeInt(i4);
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(hVar != null ? hVar.asBinder() : null);
                    parcelObtain.writeString(str2);
                    if (!this.f1570a.transact(4, parcelObtain, parcelObtain2, 0)) {
                        int i5 = a.f1569a;
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // t1.i
            public final PendingIntent h0() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IVIntentSender");
                    if (!this.f1570a.transact(3, parcelObtain, parcelObtain2, 0)) {
                        int i4 = a.f1569a;
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // t1.i
            public final int l(int i4, Intent intent, String str, h hVar, String str2, IBinder iBinder, String str3, int i5, int i6, int i7, Bundle bundle) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.core.hack.os.IVIntentSender");
                    parcelObtain.writeInt(i4);
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(hVar != null ? hVar.asBinder() : null);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeStrongBinder(iBinder);
                    parcelObtain.writeString(str3);
                    parcelObtain.writeInt(i5);
                    parcelObtain.writeInt(i6);
                    parcelObtain.writeInt(i7);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.f1570a.transact(5, parcelObtain, parcelObtain2, 0)) {
                        int i8 = a.f1569a;
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.core.hack.os.IVIntentSender");
        }

        public static i E3(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.core.hack.os.IVIntentSender");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof i)) ? new C0087a(iBinder) : (i) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5) {
            h c0086a;
            if (i4 == 1598968902) {
                parcel2.writeString("com.core.hack.os.IVIntentSender");
                return true;
            }
            if (i4 == 1) {
                parcel.enforceInterface("com.core.hack.os.IVIntentSender");
                s sVar = (s) this;
                s.a aVar = sVar.f900c;
                sVar.l(aVar.f914e, aVar.f915f, aVar.f916g, null, null, null, null, 0, 0, 0, null);
                parcel2.writeNoException();
                return true;
            }
            h c0086a2 = null;
            if (i4 == 2) {
                parcel.enforceInterface("com.core.hack.os.IVIntentSender");
                ((s) this).f1(parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                return true;
            }
            if (i4 == 3) {
                parcel.enforceInterface("com.core.hack.os.IVIntentSender");
                PendingIntent pendingIntentH0 = ((s) this).h0();
                parcel2.writeNoException();
                if (pendingIntentH0 != null) {
                    parcel2.writeInt(1);
                    pendingIntentH0.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            }
            if (i4 == 4) {
                parcel.enforceInterface("com.core.hack.os.IVIntentSender");
                int i6 = parcel.readInt();
                Intent intent = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
                String string = parcel.readString();
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder != null) {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.core.hack.os.IVIntentReceiver");
                    c0086a2 = (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof h)) ? new h.a.C0086a(strongBinder) : (h) iInterfaceQueryLocalInterface;
                }
                int iH = ((s) this).h(i6, intent, string, c0086a2, parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(iH);
                return true;
            }
            if (i4 != 5) {
                return super.onTransact(i4, parcel, parcel2, i5);
            }
            parcel.enforceInterface("com.core.hack.os.IVIntentSender");
            int i7 = parcel.readInt();
            Intent intent2 = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
            String string2 = parcel.readString();
            IBinder strongBinder2 = parcel.readStrongBinder();
            if (strongBinder2 == null) {
                c0086a = null;
            } else {
                IInterface iInterfaceQueryLocalInterface2 = strongBinder2.queryLocalInterface("com.core.hack.os.IVIntentReceiver");
                c0086a = (iInterfaceQueryLocalInterface2 == null || !(iInterfaceQueryLocalInterface2 instanceof h)) ? new h.a.C0086a(strongBinder2) : (h) iInterfaceQueryLocalInterface2;
            }
            int iL = ((s) this).l(i7, intent2, string2, c0086a, parcel.readString(), parcel.readStrongBinder(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            parcel2.writeInt(iL);
            return true;
        }
    }

    void f1(Intent intent);

    int h(int i4, Intent intent, String str, h hVar, String str2);

    PendingIntent h0();

    int l(int i4, Intent intent, String str, h hVar, String str2, IBinder iBinder, String str3, int i5, int i6, int i7, Bundle bundle);
}
