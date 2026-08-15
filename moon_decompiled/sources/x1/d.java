package x1;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.util.ArrayMap;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mDensityCompatMode")
    private static v1.c<Boolean> f1783a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mCurDefaultDisplayDpi")
    private static v1.c<Integer> f1784b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "mResourcesManager")
    public static v1.c<Object> f1785c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "mInstrumentation")
    public static v1.c<Instrumentation> f1786d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.b(name = "mBoundApplication")
    public static v1.c<Object> f1787e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.b(name = "mInitialApplication")
    public static v1.c<Application> f1788f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @v1.b(name = "mActivities")
    private static v1.c<ArrayMap<IBinder, Object>> f1789g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @v1.b(name = "mAllApplications")
    private static v1.c<List<Application>> f1790h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @v1.b(name = "mProviderMap")
    public static v1.c<ArrayMap> f1791i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @v1.e(name = "currentActivityThread", value = {})
    public static v1.h<Object> f1792j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @v1.e(name = "sendActivityResult", value = {IBinder.class, String.class, int.class, int.class, Intent.class})
    public static v1.f<Void> f1793k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @v1.e(name = "getInstrumentation", value = {})
    public static v1.f<Instrumentation> f1794l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @v1.e(name = "performNewIntents", value = {IBinder.class, List.class})
    private static v1.f<Void> f1795m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    @v1.e(name = "performNewIntents", value = {IBinder.class, List.class, boolean.class})
    private static v1.f<Void> f1796n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    @v1.e(name = "handleNewIntent", value = {IBinder.class, List.class})
    private static v1.f<Void> f1797o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    @v1.d(name = "handleNewIntent", value = {@v1.i(strings = {"android.app.ActivityThread$ActivityClientRecord"}, type = 1), @v1.i(classes = {List.class}, type = 0)})
    private static v1.f<Void> f1798p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    @v1.d(name = "handleSendResult", value = {@v1.i(strings = {"android.app.ActivityThread$ResultData"}, type = 1)})
    public static v1.f<Void> f1799q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    @v1.e(name = "handleSendResult", value = {IBinder.class, List.class, String.class})
    public static v1.f<Void> f1800r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    @v1.d(name = "handleSendResult", value = {@v1.i(strings = {"android.app.ActivityThread$ActivityClientRecord"}, type = 1), @v1.i(classes = {List.class, String.class}, type = 0)})
    public static v1.f<Void> f1801s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    @v1.d(name = "getPackageInfoNoCheck", value = {@v1.i(classes = {ApplicationInfo.class}), @v1.i(strings = {"android.content.res.CompatibilityInfo"}, type = 1)})
    public static v1.f<Object> f1802t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    @v1.e(name = "installContentProviders", value = {Context.class, List.class})
    public static v1.f<Void> f1803u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    @v1.e(name = "acquireExistingProvider", value = {Context.class, String.class, int.class, boolean.class})
    public static v1.f<IInterface> f1804v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public static Object f1805w;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "activity")
        private static v1.c<Activity> f1806a;

        static {
            j.e.q(a.class, "android.app.ActivityThread$ActivityClientRecord");
        }

        public static Activity a(Object obj) {
            v1.c<Activity> cVar = f1806a;
            if (cVar != null) {
                return cVar.a(obj);
            }
            return null;
        }
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "restrictedBackupMode")
        public static v1.c<Boolean> f1807a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<Object> f1808b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "processName")
        public static v1.c<String> f1809c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "appInfo")
        public static v1.c<ApplicationInfo> f1810d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @v1.b(name = "providers")
        public static v1.c<List<ProviderInfo>> f1811e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        @v1.b(name = "compatInfo")
        public static v1.c<Object> f1812f;

        static {
            j.e.q(b.class, "android.app.ActivityThread$AppBindData");
        }
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "", value = {String.class, int.class})
        private static v1.a<Object> f1813a;

        static {
            j.e.q(c.class, "android.app.ActivityThread$ProviderKey");
        }

        public static Object a(int i4, String str) {
            v1.a<Object> aVar = f1813a;
            if (aVar != null) {
                return aVar.a(new Object[]{str, Integer.valueOf(i4)});
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: x1.d$d, reason: collision with other inner class name */
    public static class C0098d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f1814a = j.e.q(C0098d.class, "android.app.ActivityThread$ResultData");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static Constructor<Object> f1815b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "token")
        private static v1.c<IBinder> f1816c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "results")
        private static v1.c<List> f1817d;

        public static Object a(IBinder iBinder, ArrayList arrayList) {
            Class cls;
            if (f1815b == null && (cls = f1814a) != null) {
                try {
                    Constructor<?> constructor = cls.getDeclaredConstructors()[0];
                    f1815b = constructor;
                    constructor.setAccessible(true);
                } catch (Exception unused) {
                }
            }
            Constructor<Object> constructor2 = f1815b;
            Object objNewInstance = null;
            if (constructor2 == null) {
                return null;
            }
            try {
                objNewInstance = constructor2.newInstance(new Object[0]);
                f1816c.b(objNewInstance, iBinder);
                f1817d.b(objNewInstance, arrayList);
                return objNewInstance;
            } catch (Exception unused2) {
                return objNewInstance;
            }
        }
    }

    static {
        j.e.q(d.class, "android.app.ActivityThread");
        f1805w = null;
    }

    public static synchronized Object a() {
        if (f1805w == null) {
            f1805w = f1792j.a(null);
        }
        return f1805w;
    }

    public static Integer b(Object obj) {
        v1.c<Integer> cVar = f1784b;
        if (cVar == null || obj == null) {
            return null;
        }
        return cVar.a(obj);
    }

    public static ArrayMap<IBinder, Object> c(Object obj) {
        v1.c<ArrayMap<IBinder, Object>> cVar = f1789g;
        if (cVar == null || obj == null) {
            return null;
        }
        return cVar.a(obj);
    }

    public static List<Application> d(Object obj) {
        v1.c<List<Application>> cVar = f1790h;
        if (cVar == null || obj == null) {
            return null;
        }
        return cVar.a(obj);
    }

    public static final void e(Object obj, Object obj2, ArrayList arrayList, boolean z3) {
        v1.f<Void> fVar = f1798p;
        if (fVar != null) {
            fVar.a(obj, new Object[]{obj2, arrayList});
            return;
        }
        v1.f<Void> fVar2 = f1795m;
        if (fVar2 != null) {
            fVar2.a(obj, new Object[]{obj2, arrayList});
            return;
        }
        v1.f<Void> fVar3 = f1796n;
        if (fVar3 != null) {
            fVar3.a(obj, new Object[]{obj2, arrayList, Boolean.valueOf(z3)});
            return;
        }
        v1.f<Void> fVar4 = f1797o;
        if (fVar4 != null) {
            fVar4.a(obj, new Object[]{obj2, arrayList});
        }
    }

    public static void f(int i4, int i5, Intent intent, IBinder iBinder, String str) {
        v1.f<Void> fVar = f1793k;
        if (fVar != null) {
            fVar.a(a(), new Object[]{iBinder, str, Integer.valueOf(i4), Integer.valueOf(i5), intent});
        }
    }

    public static void g(Object obj) {
        v1.c<Boolean> cVar = f1783a;
        if (cVar == null || obj == null) {
            return;
        }
        cVar.b(obj, Boolean.TRUE);
    }
}
