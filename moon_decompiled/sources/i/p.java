package i;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface p extends IInterface {

    public static abstract class a extends Binder implements p {

        /* JADX INFO: renamed from: i.p$a$a, reason: collision with other inner class name */
        public static class C0033a implements p {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static p f427b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f428a;

            static {
                perationCompat.init0(245);
            }

            public C0033a(IBinder iBinder) {
                this.f428a = iBinder;
            }

            public native String E3();

            @Override // i.p
            public native void F0(String str, List<t1.b> list, int i4);

            @Override // i.p
            public native List<t1.b> L2(String str, int i4);

            @Override // i.p
            public native void P2(String str, String str2, int i4, int i5);

            @Override // i.p
            public native void Z(String str, String str2, int i4, int i5);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.p
            public native void n0(String str, List<t1.b> list, int i4);

            @Override // i.p
            public native void u3(String str, int i4);

            @Override // i.p
            public native int w1(String str, int i4);
        }

        static {
            perationCompat.init0(653);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHNotificationManager");
        }

        public static native p E3(IBinder iBinder);

        public static native p F3();

        public static native boolean G3(p pVar);

        @Override // i.p
        public abstract /* synthetic */ void F0(String str, List<t1.b> list, int i4);

        @Override // i.p
        public abstract /* synthetic */ List<t1.b> L2(String str, int i4);

        @Override // i.p
        public abstract /* synthetic */ void P2(String str, String str2, int i4, int i5);

        @Override // i.p
        public abstract /* synthetic */ void Z(String str, String str2, int i4, int i5);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.p
        public abstract /* synthetic */ void n0(String str, List<t1.b> list, int i4);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.p
        public abstract /* synthetic */ void u3(String str, int i4);

        @Override // i.p
        public abstract /* synthetic */ int w1(String str, int i4);
    }

    void F0(String str, List<t1.b> list, int i4);

    List<t1.b> L2(String str, int i4);

    void P2(String str, String str2, int i4, int i5);

    void Z(String str, String str2, int i4, int i5);

    void n0(String str, List<t1.b> list, int i4);

    void u3(String str, int i4);

    int w1(String str, int i4);
}
