package k3;

import android.content.pm.ApplicationInfo;
import android.os.ConditionVariable;
import android.util.ArraySet;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashSet;
import k3.p;

/* JADX INFO: loaded from: classes.dex */
public final class v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ApplicationInfo f933a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f934b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f936d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f937e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final String f938f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public int f939g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public String f940h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f941i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public int f942j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public boolean f943k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public boolean f944l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public f.b f945m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public boolean f946n;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public String f948p;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public String f952t;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public final boolean f954v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public final String f955w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public final String f956x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public p.d f957y;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final HashSet f935c = new HashSet();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public final ArrayList<b> f947o = new ArrayList<>();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final ArraySet<x> f949q = new ArraySet<>();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final ArraySet<l> f950r = new ArraySet<>();

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final ArraySet<w> f951s = new ArraySet<>();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public final ConditionVariable f953u = new ConditionVariable(false);

    static {
        perationCompat.init0(302);
    }

    public v(p pVar, ApplicationInfo applicationInfo, String str, int i4, int i5, int i6) {
        this.f933a = applicationInfo;
        int i7 = applicationInfo.uid;
        this.f938f = str;
        this.f934b = i4;
        this.f936d = i5;
        this.f937e = i6;
        this.f954v = pVar.f859d.H0(applicationInfo.packageName);
        this.f955w = pVar.f859d.z3(applicationInfo.packageName);
        this.f956x = applicationInfo.packageName;
    }

    public final native void a(boolean z3);

    public final native void b(StringBuilder sb);

    public final native String toString();
}
