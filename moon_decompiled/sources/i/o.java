package i;

import android.app.job.JobInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface o extends IInterface {

    public static abstract class a extends Binder implements o {

        /* JADX INFO: renamed from: i.o$a$a, reason: collision with other inner class name */
        public static class C0032a implements o {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public static o f425b;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f426a;

            static {
                perationCompat.init0(574);
            }

            public C0032a(IBinder iBinder) {
                this.f426a = iBinder;
            }

            public native String E3();

            @Override // i.o
            public native JobInfo W1(int i4, String str, int i5);

            @Override // android.os.IInterface
            public native IBinder asBinder();

            @Override // i.o
            public native int f2(JobInfo jobInfo, String str, int i4);

            @Override // i.o
            public native List l3(String str, int i4);

            @Override // i.o
            public native int n3(int i4, String str, int i5);

            @Override // i.o
            public native void p0(String str, int i4);
        }

        static {
            perationCompat.init0(601);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.ipc.IHJobScheduler");
        }

        public static native o E3(IBinder iBinder);

        public static native o F3();

        public static native boolean G3(o oVar);

        @Override // i.o
        public abstract /* synthetic */ JobInfo W1(int i4, String str, int i5);

        @Override // android.os.IInterface
        public native IBinder asBinder();

        @Override // i.o
        public abstract /* synthetic */ int f2(JobInfo jobInfo, String str, int i4);

        @Override // i.o
        public abstract /* synthetic */ List l3(String str, int i4);

        @Override // i.o
        public abstract /* synthetic */ int n3(int i4, String str, int i5);

        @Override // android.os.Binder
        public native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // i.o
        public abstract /* synthetic */ void p0(String str, int i4);
    }

    JobInfo W1(int i4, String str, int i5);

    int f2(JobInfo jobInfo, String str, int i4);

    List l3(String str, int i4);

    int n3(int i4, String str, int i5);

    void p0(String str, int i4);
}
