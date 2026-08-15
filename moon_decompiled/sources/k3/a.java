package k3;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final p f708a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final SparseArray<b> f709b = new SparseArray<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ArrayList<x> f710c = new ArrayList<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ArrayList<x> f711d = new ArrayList<>();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final ArrayList<x> f712e = new ArrayList<>();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ArrayMap<IBinder, ArrayList<l>> f713f = new ArrayMap<>();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final int f714g = 8;

    /* JADX INFO: renamed from: k3.a$a, reason: collision with other inner class name */
    public final class C0047a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final x f715a;

        public C0047a(x xVar) {
            this.f715a = xVar;
        }
    }

    public final class b extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final ArrayMap<ComponentName, x> f716a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ArrayMap<Intent.FilterComparison, x> f717b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final ArrayList<x> f718c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final ArrayList<x> f719d;

        static {
            perationCompat.init0(380);
        }

        public b(Looper looper) {
            super(looper);
            this.f716a = new ArrayMap<>();
            this.f717b = new ArrayMap<>();
            this.f718c = new ArrayList<>();
            this.f719d = new ArrayList<>();
        }

        public final native void a();

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    static {
        perationCompat.init0(186);
    }

    public a(p pVar) {
        this.f708a = pVar;
    }

    public static native ActivityManager.RunningServiceInfo h(x xVar);

    public final native void a(v vVar, String str);

    public final native int b(f.b bVar, Intent intent, String str, IBinder iBinder, int i4, String str2, String str3, int i5);

    public final native void c(x xVar, boolean z3, boolean z4);

    public final native void d(x xVar);

    public final native String e(x xVar, boolean z3);

    public final native ArrayList f(int i4, int i5, int i6);

    public final native b g(int i4);

    public final native IBinder i(Intent intent, String str, String str2, int i4);

    public final native void j(x xVar, Intent intent, IBinder iBinder);

    public final native void k(x xVar, v vVar, boolean z3);

    public final native void l(l lVar);

    public final native boolean m(x xVar, q qVar, boolean z3);

    public final native C0047a n(Intent intent, String str, String str2, String str3, int i4, boolean z3, boolean z4, boolean z5);

    public final native void o(x xVar);

    public final native void p(x xVar, int i4, int i5, int i6);

    public final native void q(x xVar, boolean z3, boolean z4);

    public final native ComponentName r(b bVar, Intent intent, x xVar, boolean z3);

    public final native ComponentName s(f.b bVar, Intent intent, String str, int i4, int i5, String str2, int i6);

    public final native int t(f.b bVar, Intent intent, String str, int i4);

    public final native boolean u(ComponentName componentName, IBinder iBinder, int i4, int i5);

    public final native void v(x xVar, Intent intent);

    public final native boolean w(IBinder iBinder);
}
