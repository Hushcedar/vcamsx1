package r3;

import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface k extends IInterface {

    public static abstract class a extends Binder implements k {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f1386a = 0;

        /* JADX INFO: renamed from: r3.k$a$a, reason: collision with other inner class name */
        public static class C0078a implements k {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f1387a;

            static {
                perationCompat.init0(364);
            }

            public C0078a(IBinder iBinder) {
                this.f1387a = iBinder;
            }

            @Override // r3.k
            public final native int G0(PackageInstaller.SessionParams sessionParams, String str, int i4);

            @Override // r3.k
            public final native void G1(int i4);

            @Override // r3.k
            public final native PackageInstaller.SessionInfo I2(int i4);

            @Override // r3.k
            public final native void N2(int i4, boolean z3);

            @Override // r3.k
            public final native void R(int i4, String str);

            @Override // r3.k
            public final native ArrayList V0(int i4, String str);

            @Override // android.os.IInterface
            public final native IBinder asBinder();

            @Override // r3.k
            public final native void j2(IBinder iBinder);

            @Override // r3.k
            public final native void k3(int i4, Bitmap bitmap);

            @Override // r3.k
            public final native IBinder l0(int i4);

            @Override // r3.k
            public final native void r1(int i4, IBinder iBinder);

            @Override // r3.k
            public final native ArrayList x2(int i4);
        }

        static {
            perationCompat.init0(38);
        }

        public a() {
            attachInterface(this, "com.core.hack.server.pm.IPackageInstaller");
        }

        @Override // android.os.IInterface
        public final native IBinder asBinder();

        @Override // android.os.Binder
        public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    int G0(PackageInstaller.SessionParams sessionParams, String str, int i4);

    void G1(int i4);

    PackageInstaller.SessionInfo I2(int i4);

    void N2(int i4, boolean z3);

    void R(int i4, String str);

    ArrayList V0(int i4, String str);

    void j2(IBinder iBinder);

    void k3(int i4, Bitmap bitmap);

    IBinder l0(int i4);

    void r1(int i4, IBinder iBinder);

    ArrayList x2(int i4);
}
