package k3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.core.os.perationCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public boolean A;
    public final int B;
    public String C;
    public IBinder D;
    public boolean E;
    public HashSet<WeakReference<s>> F;
    public ArrayList<i3.l> G;
    public final e H;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final p f733a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final a f734b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ActivityInfo f735c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ApplicationInfo f736d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f737e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final int f738f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final String f739g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final int f740h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final Intent f741i;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final ComponentName f743k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final boolean f744l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final String f745m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final String f746n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public final String f747o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final String f748p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public a0 f749q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public int f750r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public b f751s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final String f752t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public final int f753u;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public v f755w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public int f756x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public t1.g f757y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public boolean f758z;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public ArrayList<t1.k> f754v = null;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public Intent f742j = new Intent();

    public static class a extends y3.a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final /* synthetic */ int f759c = 0;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final WeakReference<b> f760a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f761b;

        static {
            perationCompat.init0(446);
        }

        public a(b bVar, Intent intent) {
            this.f760a = new WeakReference<>(bVar);
            this.f761b = intent.getComponent().flattenToShortString();
        }

        public final native String toString();
    }

    /* JADX INFO: renamed from: k3.b$b, reason: collision with other inner class name */
    public static class C0048b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final a f762b = new a();

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final HashMap f763a = new HashMap();

        /* JADX INFO: renamed from: k3.b$b$a */
        public class a extends v3.k<C0048b> {
            static {
                perationCompat.init0(597);
            }

            @Override // v3.k
            public final native C0048b a();
        }
    }

    static {
        perationCompat.init0(185);
    }

    public b(p pVar, v vVar, ActivityInfo activityInfo, int i4, int i5, String str, Intent intent, b bVar, String str2, int i6, int i7, String str3, e eVar) {
        this.f733a = pVar;
        this.H = eVar;
        this.f734b = new a(this, intent);
        this.f735c = activityInfo;
        this.f737e = i4;
        this.f738f = i5;
        this.f739g = str;
        this.f740h = i7;
        this.f741i = intent;
        this.f751s = bVar;
        this.f752t = str2;
        this.f753u = i6;
        this.f745m = str3;
        h(1, "ActivityRecord ctor");
        this.A = false;
        String str4 = activityInfo.targetActivity;
        if (str4 != null) {
            str4.equals(intent.getComponent().getClassName());
        }
        this.f743k = intent.getComponent();
        this.f748p = activityInfo.taskAffinity;
        ApplicationInfo applicationInfo = activityInfo.applicationInfo;
        this.f736d = applicationInfo;
        this.f746n = applicationInfo.packageName;
        String str5 = activityInfo.processName;
        this.f747o = str5;
        if ((activityInfo.flags & 32) != 0) {
            intent.addFlags(8388608);
        }
        this.B = activityInfo.launchMode;
        if (vVar != null && vVar.f934b == i7 && TextUtils.equals(vVar.f938f, str5)) {
            this.f744l = true;
        } else {
            this.f744l = false;
        }
    }

    public static native b a(IBinder iBinder);

    public static native b b(IBinder iBinder);

    public final native <T extends d> T c();

    public final native void d(int i4);

    public final native void e(int i4, IBinder iBinder, int i5, String str);

    public final native void f();

    public final native void g(b bVar, String str, int i4);

    public final native boolean h(int i4, String str);

    public final native void i(a0 a0Var);

    public final native String toString();
}
