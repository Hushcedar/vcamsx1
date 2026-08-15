package i;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface l extends IInterface {

    public static abstract class a extends Binder implements l {

        /* JADX INFO: renamed from: i.l$a$a, reason: collision with other inner class name */
        public static class C0029a implements l {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static l f419b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f420a;

            static {
                perationCompat.init0(615);
            }

            public C0029a(IBinder iBinder) {
                this.f420a = iBinder;
            }

            public native String E3();

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.l
            public native boolean e0(int i4, String str);

            @Override // i.l
            public native o3.a i(int i4, String str);

            @Override // i.l
            public native boolean k2(int i4, boolean z3);
        }

        static {
            perationCompat.init0(492);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IDeviceInfoManager");
        }

        public static native l E3(IBinder iBinder);

        public static native l F3();

        public static native boolean G3(l lVar);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.l
        public abstract /* synthetic */ boolean e0(int i4, String str);

        @Override // i.l
        public abstract /* synthetic */ o3.a i(int i4, String str);

        @Override // i.l
        public abstract /* synthetic */ boolean k2(int i4, boolean z3);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    boolean e0(int i4, String str);

    o3.a i(int i4, String str);

    boolean k2(int i4, boolean z3);
}
