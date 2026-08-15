package i;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface i extends IInterface {

    public static abstract class a extends Binder implements i {

        /* JADX INFO: renamed from: i.i$a$a, reason: collision with other inner class name */
        public static class C0027a implements i {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static i f413b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f414a;

            static {
                perationCompat.init0(96);
            }

            public C0027a(IBinder iBinder) {
                this.f414a = iBinder;
            }

            public native String E3();

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.i
            public native void onBindingDied(ComponentName componentName);

            @Override // i.i
            public native void onNullBinding(ComponentName componentName);

            @Override // i.i
            public native void onServiceConnected(ComponentName componentName, IBinder iBinder);

            @Override // i.i
            public native void onServiceDisconnected(ComponentName componentName);
        }

        static {
            perationCompat.init0(321);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.HServiceConnection");
        }

        public static native i E3(IBinder iBinder);

        public static native i F3();

        public static native boolean G3(i iVar);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.i
        public abstract /* synthetic */ void onBindingDied(ComponentName componentName);

        @Override // i.i
        public abstract /* synthetic */ void onNullBinding(ComponentName componentName);

        @Override // i.i
        public abstract /* synthetic */ void onServiceConnected(ComponentName componentName, IBinder iBinder);

        @Override // i.i
        public abstract /* synthetic */ void onServiceDisconnected(ComponentName componentName);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    void onBindingDied(ComponentName componentName);

    void onNullBinding(ComponentName componentName);

    void onServiceConnected(ComponentName componentName, IBinder iBinder);

    void onServiceDisconnected(ComponentName componentName);
}
