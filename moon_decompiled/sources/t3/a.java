package t3;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Pair;
import androidx.core.os.perationCompat;
import c2.i;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import r3.n;
import r3.s;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f1579a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final e f1580b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final t3.b f1581c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Object f1582d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final n f1583e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final PackageInfo f1584f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public PackageInfo f1585g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public int f1586h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public String f1587i;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public String f1589k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public String f1590l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public String f1591m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public String[] f1592n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public String[] f1593o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public String f1594p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public String f1595q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public String f1596r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public String[] f1597s;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public Bundle f1599u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public String f1600v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public long f1601w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public final int[] f1602x;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final ArrayList f1598t = new ArrayList();

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public final HashMap f1603y = new HashMap();

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public final ArrayList f1604z = new ArrayList();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public String f1588j = i();

    /* JADX INFO: renamed from: t3.a$a, reason: collision with other inner class name */
    public static final class C0088a extends c<b> {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final ActivityInfo f1605f;

        static {
            perationCompat.init0(363);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public C0088a(e eVar, Object obj) {
            v1.c<Object> cVar;
            super(eVar, obj);
            Class cls = i.a.f159a;
            this.f1605f = (ActivityInfo) ((cls == null || !cls.isInstance(obj) || (cVar = i.a.f160b) == null) ? null : cVar.a(obj));
            v1.c<List> cVar2 = i.d.f166b;
            List listA = cVar2 != null ? cVar2.a(obj) : null;
            if (j.e.s(listA)) {
                return;
            }
            this.f1608b = new ArrayList<>(listA.size());
            Iterator it = listA.iterator();
            while (it.hasNext()) {
                this.f1608b.add(new b(this, it.next()));
            }
        }

        public final native String toString();
    }

    public static final class b extends d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final C0088a f1606e;

        static {
            perationCompat.init0(362);
        }

        public b(C0088a c0088a, Object obj) {
            super(obj);
            this.f1606e = c0088a;
        }

        public final native String toString();
    }

    public static class c<II extends d> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final e f1607a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public ArrayList<II> f1608b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f1609c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final Bundle f1610d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public ComponentName f1611e;

        static {
            perationCompat.init0(353);
        }

        public c(c<II> cVar) {
            this.f1607a = cVar.f1607a;
            this.f1608b = cVar.f1608b;
            this.f1609c = cVar.f1609c;
            this.f1610d = cVar.f1610d;
            this.f1611e = cVar.f1611e;
        }

        public c(e eVar, Object obj) {
            this.f1608b = new ArrayList<>(0);
            this.f1607a = eVar;
            v1.c<String> cVar = i.d.f165a;
            this.f1609c = cVar != null ? cVar.a(obj) : null;
            v1.c<Bundle> cVar2 = i.d.f167c;
            this.f1610d = cVar2 != null ? cVar2.a(obj) : null;
        }

        public final native void a(StringBuilder sb);

        public final native ComponentName b();
    }

    public static class d extends IntentFilter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final boolean f1612a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f1613b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final CharSequence f1614c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final int f1615d;

        public d(Object obj) {
            super((IntentFilter) obj);
            v1.c<Boolean> cVar = i.e.f168a;
            this.f1612a = Boolean.valueOf(cVar != null ? cVar.a(obj).booleanValue() : false).booleanValue();
            v1.c<Integer> cVar2 = i.e.f169b;
            this.f1613b = Integer.valueOf(cVar2 != null ? cVar2.a(obj).intValue() : 0).intValue();
            v1.c<CharSequence> cVar3 = i.e.f170c;
            this.f1614c = cVar3 != null ? cVar3.a(obj) : null;
            v1.c<Integer> cVar4 = i.e.f171d;
            this.f1615d = Integer.valueOf(cVar4 != null ? cVar4.a(obj).intValue() : 0).intValue();
            v1.c<Integer> cVar5 = i.e.f172e;
            Integer.valueOf(cVar5 != null ? cVar5.a(obj).intValue() : 0).intValue();
            v1.c<Integer> cVar6 = i.e.f173f;
            Integer.valueOf(cVar6 != null ? cVar6.a(obj).intValue() : 0).intValue();
            v1.c<Integer> cVar7 = i.e.f174g;
            Integer.valueOf(cVar7 != null ? cVar7.a(obj).intValue() : 0).intValue();
        }
    }

    public static final class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1616a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f1617b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public ApplicationInfo f1618c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public s f1619d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final a f1620e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final ArrayList<C0088a> f1621f = new ArrayList<>(0);

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public final ArrayList<C0088a> f1622g = new ArrayList<>(0);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final ArrayList<h> f1623h = new ArrayList<>(0);

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final ArrayList<j> f1624i = new ArrayList<>(0);

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public final ArrayList<f> f1625j = new ArrayList<>(0);

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public final ArrayList<g> f1626k = new ArrayList<>(0);

        static {
            perationCompat.init0(351);
        }

        public e(a aVar, String str) {
            Integer numA;
            this.f1620e = aVar;
            this.f1616a = str;
            t3.b bVar = aVar.f1581c;
            bVar.getClass();
            try {
                numA = i.g.a(bVar.f1638d);
            } catch (Exception unused) {
                numA = 0;
            }
            this.f1617b = numA.intValue();
        }

        public final native String toString();
    }

    public static class f extends c<d> {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final PermissionInfo f1627f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public g f1628g;

        static {
            perationCompat.init0(349);
        }

        public f(e eVar, Object obj) {
            v1.c<Object> cVar;
            v1.c<PermissionInfo> cVar2;
            super(eVar, obj);
            Class cls = i.C0009i.f203a;
            Object objA = null;
            this.f1627f = (cls == null || !cls.isInstance(obj) || (cVar2 = i.C0009i.f204b) == null) ? null : cVar2.a(obj);
            if (cls != null && cls.isInstance(obj) && (cVar = i.C0009i.f205c) != null) {
                objA = cVar.a(obj);
            }
            if (objA != null) {
                this.f1628g = new g(eVar, objA);
            }
        }

        public final native String toString();
    }

    public static class g extends c<d> {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final PermissionGroupInfo f1629f;

        static {
            perationCompat.init0(345);
        }

        public g(e eVar, Object obj) {
            v1.c<PermissionGroupInfo> cVar;
            super(eVar, obj);
            Class cls = i.h.f201a;
            this.f1629f = (cls == null || !cls.isInstance(obj) || (cVar = i.h.f202b) == null) ? null : cVar.a(obj);
        }

        public final native String toString();
    }

    public static final class h extends c<i> {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final ProviderInfo f1630f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public boolean f1631g;

        static {
            perationCompat.init0(343);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public h(e eVar, Object obj) {
            v1.c<Object> cVar;
            super(eVar, obj);
            Class cls = i.j.f206a;
            this.f1630f = (ProviderInfo) ((cls == null || !cls.isInstance(obj) || (cVar = i.j.f207b) == null) ? null : cVar.a(obj));
            v1.c<Boolean> cVar2 = i.j.f208c;
            this.f1631g = Boolean.valueOf(cVar2 != null ? cVar2.a(obj).booleanValue() : false).booleanValue();
            v1.c<List> cVar3 = i.d.f166b;
            List listA = cVar3 != null ? cVar3.a(obj) : null;
            if (j.e.s(listA)) {
                return;
            }
            this.f1608b = new ArrayList<>(listA.size());
            Iterator it = listA.iterator();
            while (it.hasNext()) {
                this.f1608b.add(new i(this, it.next()));
            }
        }

        public h(h hVar) {
            super(hVar);
            this.f1630f = hVar.f1630f;
            this.f1631g = hVar.f1631g;
        }

        public final native String toString();
    }

    public static final class i extends d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final h f1632e;

        static {
            perationCompat.init0(341);
        }

        public i(h hVar, Object obj) {
            super(obj);
            this.f1632e = hVar;
        }

        public final native String toString();
    }

    public static final class j extends c<k> {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final ServiceInfo f1633f;

        static {
            perationCompat.init0(340);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public j(e eVar, Object obj) {
            v1.c<Object> cVar;
            super(eVar, obj);
            Class cls = i.k.f209a;
            this.f1633f = (ServiceInfo) ((cls == null || !cls.isInstance(obj) || (cVar = i.k.f210b) == null) ? null : cVar.a(obj));
            v1.c<List> cVar2 = i.d.f166b;
            List listA = cVar2 != null ? cVar2.a(obj) : null;
            if (j.e.s(listA)) {
                return;
            }
            this.f1608b = new ArrayList<>(listA.size());
            Iterator it = listA.iterator();
            while (it.hasNext()) {
                this.f1608b.add(new k(this, it.next()));
            }
        }

        public final native String toString();
    }

    public static final class k extends d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final j f1634e;

        static {
            perationCompat.init0(360);
        }

        public k(j jVar, Object obj) {
            super(obj);
            this.f1634e = jVar;
        }

        public final native String toString();
    }

    static {
        perationCompat.init0(117);
    }

    /* JADX WARN: Removed duplicated region for block: B:140:0x0379  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public a(android.content.Context r20, r3.n r21) {
        /*
            Method dump skipped, instruction units count: 1200
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: t3.a.<init>(android.content.Context, r3.n):void");
    }

    public static final native PermissionInfo b(f fVar, int i4);

    public final native ActivityInfo a(C0088a c0088a, int i4, int i5);

    public final native ProviderInfo c(h hVar, int i4, int i5);

    public final native ApplicationInfo d(int i4, int i5);

    public final native List e(ComponentName componentName);

    public final native String f(PackageInfo packageInfo, boolean z3);

    public final native PackageInfo g(int i4, int i5);

    public final native PackageInfo h(int i4, String str);

    public final native String i();

    public final native Pair<String, String> j(PackageInfo packageInfo);

    public final native void k(ApplicationInfo applicationInfo, int i4, int i5);
}
