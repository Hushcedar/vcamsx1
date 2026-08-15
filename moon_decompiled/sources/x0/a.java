package x0;

import android.location.ILocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IRemoteCallback;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import java.util.List;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final HashMap f1760e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public LocationManager f1761f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public LocationProvider f1762g;

    /* JADX INFO: renamed from: x0.a$a, reason: collision with other inner class name */
    public class C0096a extends d {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final Integer f1763i;

        static {
            perationCompat.init0(525);
        }

        public C0096a(Integer num) {
            super();
            this.f1763i = num;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class b extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public int f1765h = -1;

        static {
            perationCompat.init0(527);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public final class c extends ILocationListener.Stub {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final ILocationListener f1766a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f1767b;

        static {
            perationCompat.init0(530);
        }

        public c(ILocationListener iLocationListener, String str) {
            this.f1766a = iLocationListener;
            this.f1767b = str;
        }

        public final native void onFlushComplete(int i4);

        public final native void onLocationChanged(Location location);

        public final native void onLocationChanged(List list, IRemoteCallback iRemoteCallback);

        public final native void onProviderDisabled(String str);

        public final native void onProviderEnabled(String str);

        public final native void onProviderEnabledChanged(String str, boolean z3);

        public final native void onRemoved();

        public final native void onStatusChanged(String str, int i4, Bundle bundle);
    }

    public class d extends com.core.hack.handle.b {
        static {
            perationCompat.init0(532);
        }

        public d() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class e extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(535);
        }

        public e() {
            super("android.location.ILocationManager");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class f extends d {
        static {
            perationCompat.init0(536);
        }

        public f() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class g extends d {
        static {
            perationCompat.init0(538);
        }

        public g() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class h extends d {
        static {
            perationCompat.init0(540);
        }

        public h() {
            super();
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(582);
    }

    public a() {
        super("android.location.ILocationManager");
        this.f1760e = new HashMap();
        this.f1761f = null;
        this.f1762g = null;
        d();
    }

    public static native void h(Object obj);

    public static native boolean i(a aVar);

    public static native c j(a aVar, ILocationListener iLocationListener, String str);

    public static native void k(j[] jVarArr, Class[] clsArr);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
