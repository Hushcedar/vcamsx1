package k3;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.m;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import k3.s;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class p extends m.a {

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public static final int f853x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public static final int f854y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public static final a f855z;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public ActivityManager f856a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f857b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public t f858c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public r3.h f859d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public k3.e f860e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public u1.g f861f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public k3.f f862g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final HashMap<IBinder, w> f863h = new HashMap<>();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final HashMap<s.a, s> f864i = new HashMap<>();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final SparseArray<s> f865j = new SparseArray<>();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public k3.a f866k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final Handler f867l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final j.e f868m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public z f869n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public y f870o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final HashMap<String, f.f> f871p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final HashSet<String> f872q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final SparseArray<Set<String>> f873r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final f f874s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public HandlerThread f875t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public Handler f876u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public final b f877v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public final e f878w;

    public class a extends v3.k<p> {
        static {
            perationCompat.init0(555);
        }

        @Override // v3.k
        public final native p a();
    }

    public class b extends i3.h<j, j> {
        static {
            perationCompat.init0(556);
        }

        @Override // i3.h
        public final native boolean c(IntentFilter intentFilter, ArrayList arrayList);

        @Override // i3.h
        public final native boolean g(IntentFilter intentFilter, String str);

        @Override // i3.h
        public final native IntentFilter[] h(int i4);

        @Override // i3.h
        public final native j i(IntentFilter intentFilter, int i4, int i5);
    }

    public final class c implements Handler.Callback {
        static {
            perationCompat.init0(551);
        }

        public c() {
        }

        @Override // android.os.Handler.Callback
        public final native boolean handleMessage(Message message);
    }

    public final class d implements IBinder.DeathRecipient {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final v f880a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f881b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final f.b f882c;

        static {
            perationCompat.init0(552);
        }

        public d(v vVar, int i4, f.b bVar) {
            this.f880a = vVar;
            this.f881b = i4;
            this.f882c = bVar;
        }

        @Override // android.os.IBinder.DeathRecipient
        public final native void binderDied();
    }

    public final class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final SparseArray<v> f884a = new SparseArray<>();

        static {
            perationCompat.init0(553);
        }

        public final native v a(int i4);

        public final native void b(v vVar);

        public final native void c(v vVar);
    }

    public class f extends v3.g {
        static {
            perationCompat.init0(554);
        }

        public f() {
            super(new File(p.a.l(), "activitymanager.list"));
        }

        @Override // v3.g, v3.f
        public final native void a(JSONObject jSONObject);

        @Override // v3.g
        public final native void i(JSONObject jSONObject);
    }

    static {
        perationCompat.init0(287);
        f853x = Process.myPid();
        f854y = Process.myUid();
        f855z = new a();
    }

    public p() {
        new SparseArray();
        this.f871p = new HashMap<>(2);
        HashSet<String> hashSet = new HashSet<>();
        this.f872q = hashSet;
        this.f873r = new SparseArray<>();
        this.f874s = new f();
        this.f877v = new b();
        this.f878w = new e();
        i3.m mVar = new i3.m("p");
        mVar.start();
        this.f867l = new Handler(mVar.getLooper(), new c());
        this.f868m = new j.e();
        hashSet.addAll(Arrays.asList("android.content.pm.action.REQUEST_PERMISSIONS", "android.intent.action.EDIT", "android.intent.action.INSERT_OR_EDIT", "android.intent.action.PICK"));
    }

    public static native void H3(p pVar, v vVar);

    public static native boolean K3(v vVar, IBinder iBinder, String str, Intent intent, int i4, int i5);

    public static native p S3();

    public static native String Z3(s sVar, String str);

    public static native void a4(v vVar, int i4);

    @Override // i.m.a, i.m
    public final native int A(int i4);

    @Override // i.m.a, i.m
    public final native IBinder A1(Intent intent, String str, String str2, int i4);

    @Override // i.m.a, i.m
    public final native int B0(f.b bVar, Intent intent, String str, int i4);

    @Override // i.m.a, i.m
    public final native boolean B2(String str, int i4, int i5);

    @Override // i.m.a, i.m
    public final native void B3(IBinder iBinder, Intent intent, boolean z3);

    @Override // i.m.a, i.m
    public final native t1.i C1(int i4, String str, IBinder iBinder, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7);

    @Override // i.m.a, i.m
    public final native boolean D3(IBinder iBinder);

    @Override // i.m.a, i.m
    public final native boolean E0(Intent intent, String str, int i4);

    @Override // i.m.a, i.m
    public final native String[] E2(int i4);

    public final native void I3(v vVar, int i4, f.b bVar, boolean z3);

    @Override // i.m.a, i.m
    public final native int J1(int i4);

    public final native void J3(int i4, int i5, f.b bVar);

    @Override // i.m.a, i.m
    public final native ComponentName K(IBinder iBinder);

    @Override // i.m.a, i.m
    public final native void L0(Intent intent, String str);

    public final native void L3(Intent intent, String str, int i4);

    @Override // i.m.a, i.m
    public final native void M1(String str, int i4, int i5);

    @Override // i.m.a, i.m
    public final native boolean M2(IBinder iBinder);

    public final native Bundle M3(String str, String str2, Bundle bundle);

    public final native void N3(s sVar, boolean z3);

    @Override // i.m.a, i.m
    public final native boolean O(IBinder iBinder, Intent intent, IBinder iBinder2);

    public final native void O3(v vVar);

    @Override // i.m.a, i.m
    public final native boolean P(int i4, IBinder iBinder, int i5, Intent intent);

    public final native void P3(Intent intent, String str, int i4);

    public final native void Q3(int i4, String str, int i5);

    @Override // i.m.a, i.m
    public final native int R0(String str, int i4, int i5, int i6, String str2);

    @Override // i.m.a, i.m
    public final native void R2(ActivityManager.RecentTaskInfo recentTaskInfo);

    public final native void R3(int i4, String str, int i5);

    @Override // i.m.a, i.m
    public final native void S0(int i4, String str, String str2, t1.g gVar);

    @Override // i.m.a, i.m
    public final native String T1(IBinder iBinder);

    @Override // i.m.a, i.m
    public final native int T2(IBinder iBinder);

    public final native int T3(int i4);

    public final native s U3(int i4, String str, int i5, int i6, IBinder iBinder, String str2, int i7, Intent[] intentArr, String[] strArr, int i8, Bundle bundle);

    @Override // i.m.a, i.m
    public final native ComponentName V2(ComponentName componentName, boolean z3);

    public final native v V3(int i4);

    @Override // i.m.a, i.m
    public final native t1.i W0(int i4);

    @Override // i.m.a, i.m
    public final native ComponentName W2(f.b bVar, Intent intent, String str, boolean z3, String str2, int i4);

    public final native v W3(int i4);

    @Override // i.m.a, i.m
    public final native String X2(int i4, String str, int i5);

    public final native v X3(int i4, String str, String str2);

    @Override // i.m.a, i.m
    public final native IBinder Y1(int i4, int i5, int i6);

    @Override // i.m.a, i.m
    public final native ActivityManager.RunningAppProcessInfo Y2();

    public final native v Y3(f.b bVar);

    @Override // i.m.a, i.m
    public final native boolean Z0(IBinder iBinder, int i4);

    @Override // i.m.a, i.m
    public final native void b2(IBinder iBinder, int i4, int i5, int i6);

    public final native void b4(int i4, String str, String str2);

    @Override // i.m.a, i.m
    public final native boolean c0(String str, int i4);

    @Override // i.m.a, i.m
    public final native r c3(IBinder iBinder);

    public final native void c4(int i4, Intent intent, List list);

    @Override // i.m.a, i.m
    public final native void d1(int i4, IBinder iBinder, int i5);

    public final native v d4(int i4, int i5, ApplicationInfo applicationInfo, String str);

    @Override // i.m.a, i.m
    public final native int e(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, String str3, int i5);

    public final native void e4(int i4, int i5, Intent intent, String str, String str2, int i6);

    @Override // i.m.a, i.m
    public final native int f3();

    @Override // i.m.a, i.m
    public final native void h2(IBinder iBinder, int i4, int i5, int i6);

    @Override // i.m.a, i.m
    public final native int i3(f.b bVar, IBinder iBinder, Intent intent, String str, IBinder iBinder2, int i4, String str2, int i5);

    @Override // i.m.a, i.m
    public final native int j(f.b bVar, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i4);

    @Override // i.m.a, i.m
    public final native String j1(IBinder iBinder, String str);

    @Override // i.m.a, i.m
    public final native void j3(int i4, IBinder iBinder, IBinder iBinder2, int i5, int i6, String str);

    @Override // i.m.a, i.m
    public final native IBinder k1(int i4);

    @Override // i.m.a, i.m
    public final native void l1(t1.i iVar, boolean z3);

    @Override // i.m.a, i.m
    public final native void m0(f.b bVar, int i4);

    @Override // i.m.a, i.m
    public final native boolean n2(ComponentName componentName, IBinder iBinder, int i4, int i5);

    @Override // i.m.a, i.m
    public final native void p3(int i4, IBinder iBinder);

    @Override // i.m.a, i.m
    public final native void q(ComponentName componentName, IBinder iBinder, int i4, Notification notification, int i5, int i6, int i7);

    @Override // i.m.a, i.m
    public final native void q3(int i4, String str, String str2, int i5);

    @Override // i.m.a, i.m
    public final native String r0(int i4);

    @Override // i.m.a, i.m
    public final native boolean r3(ActivityManager.RunningTaskInfo runningTaskInfo, int i4);

    @Override // i.m.a, i.m
    public final native void s1(int i4, IBinder iBinder);

    @Override // i.m.a, i.m
    public final native Intent s3(f.b bVar, String str, IBinder iBinder, IntentFilter intentFilter, int i4, int i5);

    @Override // i.m.a, i.m
    public final native void t0(String str, int i4, int i5, String str2);

    @Override // i.m.a, i.m
    public final native e.d u0(String str, int i4, int i5, String str2);

    @Override // i.m.a, i.m
    public final native Map u1(int i4);

    @Override // i.m.a, i.m
    public final native void v0(int i4, IBinder iBinder);

    @Override // i.m.a, i.m
    public final native void w(int i4, IBinder iBinder);

    @Override // i.m.a, i.m
    public final native String w2(IBinder iBinder);

    @Override // i.m.a, i.m
    public final native boolean w3(ActivityManager.RunningAppProcessInfo runningAppProcessInfo, int i4);

    @Override // i.m.a, i.m
    public final native int x1(f.b bVar, String str, Intent intent, String str2, IBinder iBinder, String str3, int i4, int i5, Bundle bundle, int i6, Intent intent2, i iVar);

    @Override // i.m.a, i.m
    public final native boolean x3(ActivityManager.RecentTaskInfo recentTaskInfo, int i4);

    @Override // i.m.a, i.m
    public final native boolean z1(int i4, String str, int i5);
}
