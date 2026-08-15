package k3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.ArrayMap;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class x extends Binder {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ComponentName f964a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ComponentName f965b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f966c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Intent.FilterComparison f967d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final ServiceInfo f968e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ApplicationInfo f969f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final int f970g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f971h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final String f972i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final Runnable f973j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final long f974k;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public v f977n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public long f978o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public long f979p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public int f980q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public boolean f981r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public int f982s;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final ArrayMap<Intent.FilterComparison, q> f975l = new ArrayMap<>();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final ArrayMap<IBinder, ArrayList<l>> f976m = new ArrayMap<>();

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final ArrayList<a> f983t = new ArrayList<>();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public final ArrayList<a> f984u = new ArrayList<>();

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final x f985a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final boolean f986b = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final int f987c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final Intent f988d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f989e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public int f990f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public String f991g;

        static {
            perationCompat.init0(128);
        }

        public a(x xVar, int i4, Intent intent) {
            this.f985a = xVar;
            this.f987c = i4;
            this.f988d = intent;
        }

        public final native String toString();
    }

    static {
        perationCompat.init0(253);
    }

    public x(ComponentName componentName, ComponentName componentName2, Intent.FilterComparison filterComparison, ServiceInfo serviceInfo, int i4) {
        String str;
        this.f964a = componentName;
        this.f965b = componentName2;
        this.f966c = componentName2.flattenToShortString();
        this.f967d = filterComparison;
        this.f968e = serviceInfo;
        ApplicationInfo applicationInfo = serviceInfo.applicationInfo;
        this.f969f = applicationInfo;
        this.f971h = applicationInfo.packageName;
        if ((serviceInfo.flags & 2) != 0) {
            str = serviceInfo.processName + ":" + componentName2.getClassName();
        } else {
            str = serviceInfo.processName;
        }
        this.f972i = str;
        this.f973j = null;
        this.f974k = SystemClock.elapsedRealtime();
        this.f978o = SystemClock.uptimeMillis();
        this.f970g = i4;
    }

    public final native a a(int i4, boolean z3, boolean z4);

    public final native boolean b();

    public final native h c(Intent intent, v vVar);
}
