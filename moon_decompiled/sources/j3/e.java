package j3;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountAuthenticator;
import android.accounts.IAccountAuthenticatorResponse;
import android.accounts.IAccountManagerResponse;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.k;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class e extends k.a implements m<AuthenticatorDescription> {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final Intent f574h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f575i;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public j3.b f577b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Context f578c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public c f579d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public d f580e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Account[] f576a = new Account[0];

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final SparseArray<h> f581f = new SparseArray<>();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final LinkedHashMap<String, f> f582g = new LinkedHashMap<>();

    public class a extends v3.k<e> {
        static {
            perationCompat.init0(388);
        }

        @Override // v3.k
        public final native e a();
    }

    public class b extends f {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public final String[] f583q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        public volatile Account[] f584r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        public volatile ArrayList<Account> f585s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        public volatile int f586t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        public final int f587u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        public final String f588v;

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        public final boolean f589w;

        static {
            perationCompat.init0(387);
        }

        public b(v vVar, h hVar, IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, int i4, String str2) {
            super(e.this, vVar, hVar, iAccountManagerResponse, str, false, true, null);
            this.f584r = null;
            this.f585s = null;
            this.f586t = 0;
            this.f587u = i4;
            this.f583q = strArr;
            this.f588v = str2;
            this.f589w = false;
        }

        @Override // j3.e.f
        public final native void H3();

        @Override // j3.e.f
        public final native String I3(long j4);

        public final native void J3();

        @Override // j3.e.f
        public final native void onResult(Bundle bundle);
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Context f591a;

        static {
            perationCompat.init0(386);
        }

        public c(Context context) {
            this.f591a = context;
        }

        public static native String a(int i4);

        public static native String b(int i4);

        public static native String c(int i4);
    }

    public class d extends Handler {
        static {
            perationCompat.init0(385);
        }

        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    /* JADX INFO: renamed from: j3.e$e, reason: collision with other inner class name */
    public class C0043e extends f {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public final Account f592q;

        static {
            perationCompat.init0(407);
        }

        public C0043e(v vVar, h hVar, IAccountManagerResponse iAccountManagerResponse, Account account, boolean z3) {
            super(e.this, vVar, hVar, iAccountManagerResponse, account.type, z3, true, account.name);
            this.f592q = account;
        }

        @Override // j3.e.f
        public final native void H3();

        @Override // j3.e.f
        public final native String I3(long j4);

        @Override // j3.e.f
        public final native void onResult(Bundle bundle);
    }

    public abstract class f extends IAccountAuthenticatorResponse.Stub implements IBinder.DeathRecipient, ServiceConnection {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public IAccountManagerResponse f594a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final WeakReference<v> f595b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f596c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final boolean f597d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final long f598e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final String f599f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public final boolean f600g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final boolean f601h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public int f602i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public int f603j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public int f604k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        public ComponentName f605l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public IAccountAuthenticator f606m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public final boolean f607n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        public final h f608o;

        static {
            perationCompat.init0(403);
        }

        public f(e eVar, v vVar, h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, boolean z4, String str2) {
            this(vVar, hVar, iAccountManagerResponse, str, z3, z4, str2, false, false);
        }

        public f(v vVar, h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, boolean z4, String str2, boolean z5, boolean z6) {
            this.f602i = 0;
            this.f603j = 0;
            this.f604k = 0;
            this.f606m = null;
            if (str == null) {
                throw new IllegalArgumentException("accountType is null");
            }
            this.f595b = new WeakReference<>(vVar);
            this.f608o = hVar;
            this.f607n = z4;
            this.f594a = iAccountManagerResponse;
            this.f596c = str;
            this.f597d = z3;
            this.f598e = SystemClock.elapsedRealtime();
            this.f599f = str2;
            this.f600g = z5;
            this.f601h = z6;
            synchronized (e.this.f582g) {
                e.this.f582g.put(toString(), this);
            }
            if (iAccountManagerResponse != null) {
                try {
                    iAccountManagerResponse.asBinder().linkToDeath(this, 0);
                } catch (RemoteException unused) {
                    this.f594a = null;
                    binderDied();
                }
            }
        }

        public final native void E3();

        public final native void F3(ComponentName componentName, Bundle bundle);

        public final native IAccountManagerResponse G3();

        public abstract void H3();

        public native String I3(long j4);

        @Override // android.os.IBinder.DeathRecipient
        public final native void binderDied();

        public final native void close();

        public final native void onError(int i4, String str);

        public final native void onRequestContinued();

        public native void onResult(Bundle bundle);

        @Override // android.content.ServiceConnection
        public final native void onServiceConnected(ComponentName componentName, IBinder iBinder);

        @Override // android.content.ServiceConnection
        public final native void onServiceDisconnected(ComponentName componentName);

        public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class g extends f {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public final String[] f610q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        public final Account f611r;

        static {
            perationCompat.init0(401);
        }

        public g(e eVar, v vVar, h hVar, IAccountManagerResponse iAccountManagerResponse, Account account, String[] strArr) {
            super(eVar, vVar, hVar, iAccountManagerResponse, account.type, false, true, account.name);
            this.f610q = strArr;
            this.f611r = account;
        }

        @Override // j3.e.f
        public final native void H3();

        @Override // j3.e.f
        public final native String I3(long j4);

        @Override // j3.e.f
        public final native void onResult(Bundle bundle);
    }

    public static class h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f612a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final j3.c f613b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final Object f614c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final Object f615d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final LinkedHashMap f616e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final HashMap f617f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public final HashMap f618g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final n f619h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final HashMap f620i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final HashMap f621j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public final HashMap<Account, AtomicReference<String>> f622k;

        public h(Context context, int i4, File file, File file2) {
            Object obj = new Object();
            this.f614c = obj;
            Object obj2 = new Object();
            this.f615d = obj2;
            this.f616e = new LinkedHashMap();
            this.f617f = new HashMap();
            this.f618g = new HashMap();
            this.f619h = new n();
            this.f620i = new HashMap();
            this.f621j = new HashMap();
            this.f622k = new HashMap<>();
            this.f612a = i4;
            synchronized (obj2) {
                synchronized (obj) {
                    this.f613b = j3.c.c(context, i4, file, file2);
                }
            }
        }
    }

    static {
        perationCompat.init0(176);
        f574h = new Intent("android.accounts.LOGIN_ACCOUNTS_CHANGED");
        f575i = new a();
    }

    public static native boolean H3(Account account, h hVar);

    public static native Account[] J3(h hVar, Account[] accountArr, int i4, String str, boolean z3);

    public static native e K3();

    public static native int M3(Account account, h hVar, String str);

    public static native LinkedHashMap N3(String str, ArrayList arrayList, h hVar);

    public static native LinkedHashMap R3(j3.b bVar, int i4);

    public static native Map T3(Account account, h hVar);

    public static native HashMap U3(Account account, h hVar);

    public static native Account X3(Account account, h hVar);

    public static native ArrayList Y3(h hVar, String str, String str2);

    public static native void Z3(String str, h hVar);

    public static native void a4(h hVar);

    public static native String b4(Account account, h hVar, String str);

    public static native String c4(h hVar, Account account, String str, String str2, byte[] bArr);

    public static native String d4(Account account, h hVar);

    public static native String e4(Account account, h hVar);

    public static native String f4(Account account, h hVar, String str);

    public static native void g4(String[] strArr, String str, h hVar);

    public static native void h4(Account account, h hVar);

    public static native Integer k4(Account account, h hVar, String str);

    public static native boolean l4(h hVar, Account account, String str, String str2);

    public static native void m4(Account account, String str, int i4);

    public static native void n4(int i4);

    public static native void o4(Account account, h hVar);

    public static native void q4(Account account, h hVar, String str);

    public static native void r4(h hVar, Account account, String str, String str2);

    public static native void t4(String[] strArr, String str, h hVar);

    public static native boolean u4(int i4, Account account, h hVar, String str);

    public static native void w4(h hVar, Account account, String str, String str2);

    @Override // i.k.a, i.k
    public final native void A0(IBinder iBinder, Account account, boolean z3, int i4);

    @Override // i.k.a, i.k
    public final native boolean A2(Account account, String str, Bundle bundle, Map map);

    @Override // i.k.a, i.k
    public final native boolean B1(Account account);

    @Override // i.k.a, i.k
    public final native void C(String[] strArr, String str);

    @Override // i.k.a, i.k
    public final native Map<String, Integer> D2(Account account);

    @Override // i.k.a, i.k
    public final native Map<Account, Integer> E1(String str, String str2);

    @Override // i.k.a, i.k
    public final native void I0(IBinder iBinder, Account account, String[] strArr, String str);

    public final native boolean I3(h hVar, Account account, String str, Bundle bundle, Map map);

    @Override // i.k.a, i.k
    public final native boolean J0(Account account, String str, Bundle bundle);

    @Override // i.k.a, i.k
    public final native void K1(IBinder iBinder, String str, boolean z3);

    @Override // i.k.a, i.k
    public final native String K2(Account account, String str);

    public final native ArrayList L3(Account account, h hVar);

    @Override // i.k.a, i.k
    public final native String O1(Account account, String str);

    public final native Account[] O3(String str, int i4, String str2, int i5, String str3, boolean z3);

    public final native Account[] P3(h hVar, String str, int i4, String str2, boolean z3);

    public final native Account[] Q3(h hVar, int i4, String str, ArrayList arrayList, boolean z3);

    public final native AuthenticatorDescription[] S3(int i4);

    @Override // i.k.a, i.k
    public final native boolean U0(Account account, String str, int i4);

    @Override // i.k.a, i.k
    public final native Account[] U1(String str, String str2, String str3);

    @Override // i.k.a, i.k
    public final native void V1(IBinder iBinder, Account account, String str, boolean z3, boolean z4, Bundle bundle);

    public final native ArrayList V3(int i4, int i5, boolean z3);

    public final native h W3(int i4);

    @Override // i.k.a, i.k
    public final native void Y0(IBinder iBinder, String str, String str2, String[] strArr, boolean z3, Bundle bundle);

    @Override // i.k.a, i.k
    public final native int a2(Account account, String str);

    @Override // i.k.a, i.k
    public final native void b0(IBinder iBinder, Account account, Bundle bundle, boolean z3, int i4);

    @Override // i.k.a, i.k
    public final native void b1(IBinder iBinder, Account account, String str, boolean z3, Bundle bundle);

    @Override // i.k.a, i.k
    public final native void c(Account account, String str);

    @Override // i.k.a, i.k
    public final native void c2(IBinder iBinder, Account account, String str);

    @Override // i.k.a, i.k
    public final native void d(String str, String str2);

    @Override // i.k.a, i.k
    public final native void e2(IBinder iBinder, String str, String[] strArr, String str2);

    @Override // i.k.a, i.k
    public final native boolean f(Account account);

    @Override // i.k.a, i.k
    public final native void g1(String[] strArr, String str);

    public final native boolean i4(h hVar, Account account);

    public final native Account j4(Account account, h hVar, String str);

    @Override // i.k.a, i.k
    public final native Account[] m2(String str, int i4, String str2);

    @Override // i.k.a, i.k
    public final native String m3(Account account);

    @Override // i.k.a, i.k
    public final native String p(Account account);

    public final native boolean p4(Account account, String str, int i4, boolean z3, h hVar);

    @Override // i.k.a, i.k
    public final native void s(IBinder iBinder, String str, String str2);

    public final native void s4(h hVar);

    @Override // i.k.a, i.k
    public final native void t2(Account account);

    @Override // i.k.a, i.k
    public final native void v1(Account account, String str, String str2);

    public final native void v4(h hVar, boolean z3);

    @Override // i.k.a, i.k
    public final native void y1(Account account, String str, String str2);

    @Override // i.k.a, i.k
    public final native AuthenticatorDescription[] y2(int i4);
}
