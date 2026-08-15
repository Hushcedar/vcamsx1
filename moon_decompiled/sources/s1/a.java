package s1;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class a extends Binder implements IInterface {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final /* synthetic */ int f1462d = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final HashMap<String, com.core.hack.handle.a> f1463a = new HashMap<>(7);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public b f1464b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Object f1465c;

    /* JADX INFO: renamed from: s1.a$a, reason: collision with other inner class name */
    public class C0081a extends com.core.hack.handle.a {
        static {
            perationCompat.init0(508);
        }

        public C0081a() {
        }

        @Override // com.core.hack.handle.a
        public final native Object m(Object[] objArr);
    }

    public class b extends r1.a {
        static {
            perationCompat.init0(503);
        }

        public b(Object obj) {
            super(obj);
            d();
        }

        @Override // com.core.hack.handle.c
        public final native HashMap<String, com.core.hack.handle.b> c();
    }

    static {
        perationCompat.init0(94);
    }

    public a(Object obj) {
        attachInterface(this, "android.app.IApplicationThread$wrapper");
        this.f1465c = obj;
        C0081a[] c0081aArr = new C0081a[7];
        for (int i4 = 0; i4 < 7; i4++) {
            c0081aArr[i4] = new C0081a();
        }
        C0081a c0081a = c0081aArr[0];
        HashMap<String, com.core.hack.handle.a> map = this.f1463a;
        map.put("scheduleCreateService", c0081a);
        map.put("scheduleStopService", c0081aArr[1]);
        map.put("scheduleServiceArgs", c0081aArr[2]);
        map.put("scheduleBindService", c0081aArr[3]);
        map.put("scheduleUnbindService", c0081aArr[4]);
        map.put("scheduleReceiver", c0081aArr[5]);
        map.put("scheduleInstallProvider", c0081aArr[6]);
        this.f1464b = new b(obj);
    }

    public static native com.core.hack.handle.a E3(String str);

    @Override // android.os.IInterface
    public final native IBinder asBinder();

    @Override // android.os.Binder
    public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
}
