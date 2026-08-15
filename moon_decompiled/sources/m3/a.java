package m3;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncRequest;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.n;
import i.s;
import i.t;
import java.util.ArrayList;
import java.util.List;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a extends n.a {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final C0059a f1047h;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1048a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final d f1049b = new d("");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public m3.d f1050c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Object f1051d = new Object();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final HandlerThread f1052e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f1053f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final SparseArray<ArrayMap<String, ArrayMap<Pair<String, Uri>, Bundle>>> f1054g;

    /* JADX INFO: renamed from: m3.a$a, reason: collision with other inner class name */
    public class C0059a extends k<a> {
        static {
            perationCompat.init0(70);
        }

        @Override // v3.k
        public final native a a();
    }

    public class b extends BroadcastReceiver {
        static {
            perationCompat.init0(73);
        }

        public b() {
        }

        @Override // android.content.BroadcastReceiver
        public final native void onReceive(Context context, Intent intent);
    }

    public static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final d f1056a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final IContentObserver f1057b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final boolean f1058c;

        public c(d dVar, IContentObserver iContentObserver, boolean z3) {
            this.f1056a = dVar;
            this.f1057b = iContentObserver;
            this.f1058c = z3;
        }
    }

    public static final class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1059a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ArrayList<d> f1060b = new ArrayList<>();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final ArrayList<C0060a> f1061c = new ArrayList<>();

        /* JADX INFO: renamed from: m3.a$d$a, reason: collision with other inner class name */
        public class C0060a implements IBinder.DeathRecipient {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IContentObserver f1062a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public final boolean f1063b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public final int f1064c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public final Object f1065d;

            static {
                perationCompat.init0(208);
            }

            public C0060a(IContentObserver iContentObserver, boolean z3, d dVar, int i4) {
                this.f1065d = dVar;
                this.f1062a = iContentObserver;
                this.f1064c = i4;
                this.f1063b = z3;
                try {
                    iContentObserver.asBinder().linkToDeath(this, 0);
                } catch (RemoteException unused) {
                    binderDied();
                }
            }

            @Override // android.os.IBinder.DeathRecipient
            public final native void binderDied();
        }

        static {
            perationCompat.init0(79);
        }

        public d(String str) {
            this.f1059a = str;
        }

        public final native void a(Uri uri, int i4, IContentObserver iContentObserver, boolean z3, d dVar, int i5, int i6, int i7);

        public final native void b(boolean z3, IContentObserver iContentObserver, boolean z4, int i4, int i5, ArrayList<c> arrayList);

        public final native void c(Uri uri, int i4, IContentObserver iContentObserver, boolean z3, int i5, int i6, ArrayList<c> arrayList);

        public final native boolean d(IContentObserver iContentObserver);
    }

    public class e extends s.a {
        static {
            perationCompat.init0(78);
        }

        public e() {
        }

        @Override // i.s.a, i.s
        public final native void o0(String str, int i4, String str2, int i5);
    }

    public class f extends t.a {
        static {
            perationCompat.init0(80);
        }

        public f() {
        }

        @Override // i.t.a, i.t
        public final native void B(String str, int i4, String str2, Bundle bundle, int i5);
    }

    static {
        perationCompat.init0(357);
        f1047h = new C0059a();
    }

    public a() {
        new f();
        new e();
        this.f1052e = new HandlerThread("ContentService");
        this.f1053f = false;
        this.f1054g = new SparseArray<>();
        new b();
    }

    public static native a I3();

    public static native String J3(Uri uri, int i4);

    @Override // i.n.a, i.n
    public final native void A3(int i4, IBinder iBinder);

    @Override // i.n.a, i.n
    public final native boolean D(Account account, String str, ComponentName componentName);

    @Override // i.n.a, i.n
    public final native void G(SyncRequest syncRequest);

    public final native ArrayMap<Pair<String, Uri>, Bundle> H3(int i4, String str);

    @Override // i.n.a, i.n
    public final native SyncAdapterType[] I(int i4);

    @Override // i.n.a, i.n
    public final native void J2(Account account, String str, Bundle bundle, long j4);

    public final native m3.d K3();

    public final native void L3(String str, Uri uri, int i4);

    @Override // i.n.a, i.n
    public final native void M(Account account, String str, int i4);

    @Override // i.n.a, i.n
    public final native void N(Uri uri, boolean z3, IBinder iBinder, int i4, int i5);

    @Override // i.n.a, i.n
    public final native boolean O2(Account account, String str, ComponentName componentName, int i4);

    @Override // i.n.a, i.n
    public final native boolean P0();

    @Override // i.n.a, i.n
    public final native Bundle Q0(String str, Uri uri, int i4);

    @Override // i.n.a, i.n
    public final native void S(Account account, String str, boolean z3, int i4);

    @Override // i.n.a, i.n
    public final native boolean S1(Account account, String str, ComponentName componentName);

    @Override // i.n.a, i.n
    public final native void T(Account account, String str, Bundle bundle);

    @Override // i.n.a, i.n
    public final native void U2(Account account, String str, ComponentName componentName);

    @Override // i.n.a, i.n
    public final native List<PeriodicSync> Y(Account account, String str, ComponentName componentName);

    @Override // i.n.a, i.n
    public final native void Z2(Account account, String str, ComponentName componentName, int i4);

    @Override // i.n.a, i.n
    public final native void a3(String str, Uri uri, Bundle bundle, int i4);

    @Override // i.n.a, i.n
    public final native void d2(boolean z3);

    @Override // i.n.a, i.n
    public final native void d3(SyncRequest syncRequest, int i4);

    @Override // i.n.a, i.n
    public final native List<m3.c> e1();

    @Override // i.n.a, i.n
    public final native List<m3.c> f0(int i4);

    @Override // i.n.a, i.n
    public final native String[] g2(String str, int i4);

    @Override // i.n.a, i.n
    public final native void j0(Account account, String str, boolean z3);

    @Override // i.n.a, i.n
    public final native boolean k(Account account, String str);

    @Override // i.n.a, i.n
    public final native void k0(Account account, String str, Bundle bundle);

    @Override // i.n.a, i.n
    public final native void m(boolean z3, int i4);

    @Override // i.n.a, i.n
    public final native void m1(IBinder iBinder);

    @Override // i.n.a, i.n
    public final native int n1(Account account, String str);

    @Override // i.n.a, i.n
    public final native int o3(Account account, String str, int i4);

    @Override // i.n.a, android.os.Binder
    public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);

    @Override // i.n.a, i.n
    public final native void s0(SyncRequest syncRequest);

    @Override // i.n.a, i.n
    public final native SyncAdapterType[] s2();

    @Override // i.n.a, i.n
    public final native boolean t(int i4);

    @Override // i.n.a, i.n
    public final native void t1(Uri uri, IBinder iBinder, boolean z3, int i4, int i5, int i6);

    @Override // i.n.a, i.n
    public final native boolean y(Account account, String str, int i4);

    @Override // i.n.a, i.n
    public final native void y0(IBinder iBinder);
}
