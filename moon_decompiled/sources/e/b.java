package e;

import android.app.Activity;
import android.app.Application;
import android.app.IServiceConnection;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import androidx.core.os.perationCompat;
import f.b;
import i3.o;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class b extends b.a implements Thread.UncaughtExceptionHandler {

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public static volatile b f281x;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Context f282b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final f f283c = new f();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f284d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public Application f285e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public a f286f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public Object f287g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public Object f288h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public Object f289i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final ArrayList f290j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final HashSet<IBinder> f291k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public ArrayMap<IBinder, Object> f292l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public ArrayMap<Object, Object> f293m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final HashMap f294n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public final ConditionVariable f295o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final ConditionVariable f296p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final HashMap<IBinder, e> f297q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final HashMap f298r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public List<Application> f299s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final C0014b f300t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public final o f301u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public final ConditionVariable f302v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public final ArrayMap<IBinder, Service> f303w;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public Object f304a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f305b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public ApplicationInfo f306c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public List<ProviderInfo> f307d;
    }

    /* JADX INFO: renamed from: e.b$b, reason: collision with other inner class name */
    public class C0014b extends d.a {
        public C0014b() {
        }
    }

    public static final class c {
        static {
            perationCompat.init0(464);
        }

        public final native String toString();
    }

    public static final class d {
        static {
            perationCompat.init0(495);
        }

        public final native String toString();
    }

    public static final class e implements ServiceConnection {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final IServiceConnection f309a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f310b;

        static {
            perationCompat.init0(497);
        }

        public e(IServiceConnection iServiceConnection, String str) {
            this.f309a = iServiceConnection;
            this.f310b = str;
        }

        @Override // android.content.ServiceConnection
        public final native void onBindingDied(ComponentName componentName);

        @Override // android.content.ServiceConnection
        public final native void onServiceConnected(ComponentName componentName, IBinder iBinder);

        @Override // android.content.ServiceConnection
        public final native void onServiceDisconnected(ComponentName componentName);
    }

    public class f extends Handler {
        static {
            perationCompat.init0(490);
        }

        public f() {
            super(Looper.getMainLooper());
        }

        public final native void a(Message message);

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    public static final class g {
        static {
            perationCompat.init0(491);
        }

        public final native String toString();
    }

    public static final class h {
        static {
            perationCompat.init0(485);
        }

        public final native String toString();
    }

    public final class i implements ServiceConnection {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final i.i f312a;

        static {
            perationCompat.init0(488);
        }

        public i(i.i iVar) {
            this.f312a = iVar;
        }

        @Override // android.content.ServiceConnection
        public final native void onServiceConnected(ComponentName componentName, IBinder iBinder);

        @Override // android.content.ServiceConnection
        public final native void onServiceDisconnected(ComponentName componentName);
    }

    static {
        perationCompat.init0(211);
    }

    public b(Context context, int i4) {
        Looper.getMainLooper();
        this.f290j = new ArrayList();
        this.f291k = new HashSet<>(2);
        this.f294n = new HashMap();
        this.f295o = new ConditionVariable(false);
        this.f296p = new ConditionVariable(false);
        this.f297q = new HashMap<>();
        this.f298r = new HashMap();
        this.f302v = new ConditionVariable(false);
        this.f303w = new ArrayMap<>();
        this.f282b = context;
        u1.c.a().g();
        C0014b c0014b = new C0014b();
        this.f300t = c0014b;
        this.f301u = new o(c0014b);
        Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Q3(100, Integer.valueOf(i4));
    }

    public static native void G3(PendingIntent pendingIntent);

    public static native PendingIntent H3(PendingIntent pendingIntent);

    public static native void I3(IBinder iBinder);

    public static native void O3(Context context, int i4);

    public static native int R3(Intent intent, Bundle bundle, IBinder iBinder, int i4, int i5);

    @Override // f.b
    public final native void C2(IBinder iBinder, Intent intent, int i4, String str, Bundle bundle, boolean z3, boolean z4, int i5);

    @Override // f.b
    public final native boolean D0(Intent intent, i.i iVar, int i4);

    @Override // f.b
    public final native void F1(String str, t1.g gVar);

    public final native void F3();

    @Override // f.b
    public final native void G2();

    @Override // f.b
    public final native boolean H1(i.i iVar, int i4);

    public final native Activity J3(IBinder iBinder);

    public final native Context K3();

    public final native IBinder L3(ProviderInfo providerInfo);

    @Override // f.b
    public final native void M0(i3.e eVar);

    public final native void M3(int i4, String str, boolean z3);

    public final native boolean N3(IBinder iBinder);

    public final native boolean P3();

    @Override // f.b
    public final native void Q1(IBinder iBinder);

    public final native void Q3(int i4, Object obj);

    @Override // f.b
    public final native void R1(IBinder iBinder, Intent intent, boolean z3, int i4);

    @Override // f.b
    public final native void g0(Intent intent, ActivityInfo activityInfo);

    @Override // f.b
    public final native void g3(IBinder iBinder, ServiceInfo serviceInfo, int i4);

    @Override // f.b
    public final native void p1(String str, ApplicationInfo applicationInfo, List<ProviderInfo> list, ComponentName componentName);

    @Override // f.b
    public final native void q0(String str, f.c cVar, int i4);

    @Override // f.b
    public final native void q2(IBinder iBinder, ArrayList arrayList);

    @Override // f.b
    public final native void r2(IBinder iBinder, Intent intent);

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final native void uncaughtException(Thread thread, Throwable th);

    @Override // f.b
    public final native boolean v(ProviderInfo providerInfo);

    @Override // f.b
    public final native IBinder z(ProviderInfo providerInfo, boolean z3);
}
