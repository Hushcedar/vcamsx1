package i;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface r extends IInterface {

    public static abstract class a extends Binder implements r {

        /* JADX INFO: renamed from: i.r$a$a, reason: collision with other inner class name */
        public static class C0035a implements r {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static r f431b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f432a;

            static {
                perationCompat.init0(89);
            }

            public C0035a(IBinder iBinder) {
                this.f432a = iBinder;
            }

            public native String E3();

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.r
            public native IBinder i2(String str);
        }

        static {
            perationCompat.init0(725);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHServiceManager");
        }

        public static native r E3(IBinder iBinder);

        public static native r F3();

        public static native boolean G3(r rVar);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.r
        public abstract /* synthetic */ IBinder i2(String str);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    IBinder i2(String str);
}
