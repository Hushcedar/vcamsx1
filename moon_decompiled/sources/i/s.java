package i;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface s extends IInterface {

    public static abstract class a extends Binder implements s {

        /* JADX INFO: renamed from: i.s$a$a, reason: collision with other inner class name */
        public static class C0036a implements s {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static s f433b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f434a;

            static {
                perationCompat.init0(192);
            }

            public C0036a(IBinder iBinder) {
                this.f434a = iBinder;
            }

            public native String E3();

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.s
            public native void o0(String str, int i4, String str2, int i5);
        }

        static {
            perationCompat.init0(25);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IPackageDeleteObserver2");
        }

        public static native s E3(IBinder iBinder);

        public static native s F3();

        public static native boolean G3(s sVar);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        public abstract /* synthetic */ void o0(String str, int i4, String str2, int i5);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    void o0(String str, int i4, String str2, int i5);
}
