package r3;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallbackList;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.q;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class h extends q.a {

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public static final a f1352y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public static final r3.c f1353z;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f1355b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public PackageManager f1356c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public r3.d f1357d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public i f1358e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public s3.e f1359f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public ComponentName f1360g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public x f1361h;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public d f1370q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public HandlerThread f1371r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public w f1372s;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public String f1374u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public List<String> f1375v;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ArrayList f1354a = new ArrayList();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final RemoteCallbackList<i.t> f1362i = new RemoteCallbackList<>();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final RemoteCallbackList<i.s> f1363j = new RemoteCallbackList<>();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final HashMap f1364k = new HashMap();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final SparseArray<s> f1365l = new SparseArray<>();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final HashMap f1366m = new HashMap();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final e f1367n = new e();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public final ConditionVariable f1368o = new ConditionVariable(false);

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final ConditionVariable f1369p = new ConditionVariable(false);

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public boolean f1373t = true;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public final HashSet f1376w = new HashSet(20);

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public final b f1377x = new b();

    public class a extends v3.k<h> {
        static {
            perationCompat.init0(663);
        }

        @Override // v3.k
        public final native h a();
    }

    public class b extends BroadcastReceiver {
        static {
            perationCompat.init0(662);
        }

        public b() {
        }

        @Override // android.content.BroadcastReceiver
        public final native void onReceive(Context context, Intent intent);
    }

    public class c implements u3.a {
        public c() {
        }
    }

    public class d extends Handler {
        static {
            perationCompat.init0(660);
        }

        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    public class e extends v3.g {
        static {
            perationCompat.init0(661);
        }

        public e() {
            super(new File(p.a.l(), "packages.list"));
        }

        @Override // v3.g, v3.f
        public final native void a(JSONObject jSONObject);

        @Override // v3.g
        public final native void i(JSONObject jSONObject);
    }

    static {
        perationCompat.init0(747);
        f1352y = new a();
        f1353z = new r3.c(1);
    }

    public static native void H3(h hVar, String str);

    public static native h M3();

    public static native int d4(int i4, int i5);

    @Override // i.q.a, i.q
    public final native PermissionGroupInfo C0(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native int C3(int i4, String str, int i5);

    @Override // i.q.a, i.q
    public final native IBinder D1(Intent intent, String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native IBinder E(int i4, int i5);

    @Override // i.q.a, i.q
    public final native PackageInfo F(String str, int i4);

    @Override // i.q.a, i.q
    public final native ActivityInfo F2(ComponentName componentName, int i4, int i5);

    @Override // i.q.a, i.q
    public final native boolean H0(String str);

    @Override // i.q.a, i.q
    public final native void H2(ComponentName componentName, int i4, int i5, int i6);

    @Override // i.q.a, i.q
    public final native PermissionInfo I1(String str, int i4, int i5);

    public final native s I3(int i4, String str);

    @Override // i.q.a, i.q
    public final native List<IntentFilter> J(ComponentName componentName, int i4);

    public final native int J3(String str, String str2, String str3);

    @Override // i.q.a, i.q
    public final native int K0(String str, String str2, int i4);

    public final native void K3(s sVar, int i4, String str, int i5);

    @Override // i.q.a, i.q
    public final native void L(long j4);

    @Override // i.q.a, i.q
    public final native ActivityInfo L1(ComponentName componentName, int i4, int i5);

    public final native void L3(s sVar, int i4, String str, int i5);

    @Override // i.q.a, i.q
    public final native String N0(int i4, int i5);

    @Override // i.q.a, i.q
    public final native int N1(String str, String str2, int i4, String str3);

    public final native ActivityInfo N3(ComponentName componentName, int i4, int i5);

    @Override // i.q.a, i.q
    public final native int[] O0();

    public final native String O3(String str, s sVar);

    @Override // i.q.a, i.q
    public final native ProviderInfo P1(ComponentName componentName, int i4, int i5);

    public final native s P3(String str);

    @Override // i.q.a, i.q
    public final native List<SharedLibraryInfo> Q(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native IBinder Q2(Intent intent, String str, int i4, int i5);

    public final native String[] Q3(int i4);

    public final native List<Integer> R3(int i4);

    @Override // i.q.a, i.q
    public final native IBinder S2(int i4, int i5);

    public final native m S3(l lVar);

    @Override // i.q.a, i.q
    public final native IBinder T0(Intent intent, String str, int i4, int i5);

    public final native boolean T3(ComponentInfo componentInfo, int i4, int i5);

    @Override // i.q.a, i.q
    public final native boolean U(String str, int i4);

    public final native boolean U3(ComponentInfo componentInfo, int i4, int i5);

    @Override // i.q.a, i.q
    public final native ResolveInfo V(Intent intent, String str, int i4, int i5);

    public final native boolean V3(n nVar);

    public final native void W3(int i4, String str, int i5, int i6);

    @Override // i.q.a, i.q
    public final native ResolveInfo X(Intent intent, int i4);

    @Override // i.q.a, i.q
    public final native ApplicationInfo X0(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native s X1(String str, int i4);

    public final native List X3(String str, int i4, int i5, int i6);

    public final native List<ResolveInfo> Y3(Intent intent, String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native IBinder Z1(int i4, int i5);

    public final native List Z3(Intent intent, String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native void a(i.t tVar);

    @Override // i.q.a, i.q
    public final native PackageInfo a0(int i4, String str, int i5);

    public final native void a4(j jVar);

    @Override // i.q.a, i.q
    public final native ProviderInfo b(String str, String str2, int i4, int i5);

    @Override // i.q.a, i.q
    public final native int[] b3(String str);

    public final native ProviderInfo b4(String str, int i4, int i5, boolean z3);

    public final native void c4(ArrayList arrayList);

    @Override // i.q.a, i.q
    public final native boolean d0(String str);

    @Override // i.q.a, i.q
    public final native boolean e3(String str, int i4);

    @Override // i.q.a, i.q
    public final native String[] g(int i4, int i5, int i6);

    @Override // i.q.a, i.q
    public final native ResolveInfo h1(Intent intent, int i4);

    @Override // i.q.a, i.q
    public final native Map h3(int i4, int i5);

    @Override // i.q.a, i.q
    public final native IBinder i0();

    @Override // i.q.a, i.q
    public final native ResolveInfo i1(Intent intent, String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native int l2(String str, int i4);

    @Override // i.q.a, i.q
    public final native int n(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr);

    @Override // i.q.a, i.q
    public final native ServiceInfo o(ComponentName componentName, int i4, int i5);

    @Override // i.q.a, i.q
    public final native List o1(Intent intent, String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native IBinder o2(String str, int i4, int i5, int i6);

    @Override // i.q.a, i.q
    public final native int p2(String str, int i4);

    @Override // i.q.a, i.q
    public final native boolean q1(String str);

    @Override // i.q.a, i.q
    public final native Bundle r(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native void t3(i.s sVar);

    @Override // i.q.a, i.q
    public final native void u(i.s sVar);

    @Override // i.q.a, i.q
    public final native IBinder u2(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native boolean v2(String str, int i4, int i5, boolean z3);

    @Override // i.q.a, i.q
    public final native void v3(int i4, String str, int i5, int i6);

    @Override // i.q.a, i.q
    public final native IBinder w0(int i4, int i5);

    @Override // i.q.a, i.q
    public final native void x(i.t tVar);

    @Override // i.q.a, i.q
    public final native int x0(ComponentName componentName, int i4);

    @Override // i.q.a, i.q
    public final native int y3(int i4, String str, int i5);

    @Override // i.q.a, i.q
    public final native int z0(int i4, String str, int i5);

    @Override // i.q.a, i.q
    public final native boolean z2(String str, int i4, int i5);

    @Override // i.q.a, i.q
    public final native String z3(String str);
}
