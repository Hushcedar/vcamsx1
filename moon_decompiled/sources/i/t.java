package i;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface t extends IInterface {

    public static abstract class a extends Binder implements t {

        /* JADX INFO: renamed from: i.t$a$a, reason: collision with other inner class name */
        public static class C0037a implements t {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static t f435b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f436a;

            static {
                perationCompat.init0(546);
            }

            public C0037a(IBinder iBinder) {
                this.f436a = iBinder;
            }

            @Override // i.t
            public native void B(String str, int i4, String str2, Bundle bundle, int i5);

            public native String E3();

            @Override // android.os.IInterface
            public native IBinder asBinder();
        }

        static {
            perationCompat.init0(75);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IPackageInstallObserver2");
        }

        public static native t E3(IBinder iBinder);

        public static native t F3();

        public static native boolean G3(t tVar);

        public abstract /* synthetic */ void B(String str, int i4, String str2, Bundle bundle, int i5);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    void B(String str, int i4, String str2, Bundle bundle, int i5);
}
