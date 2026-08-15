package c2;

import android.content.pm.ApplicationInfo;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "scanSourceDir")
    private static v1.c<String> f94a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "scanPublicSourceDir")
    private static v1.c<String> f95b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "primaryCpuAbi")
    private static v1.c<String> f96c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "secondaryCpuAbi")
    private static v1.c<String> f97d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.b(name = "secondaryNativeLibraryDir")
    private static v1.c<String> f98e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.b(name = "credentialProtectedDataDir")
    private static v1.c<String> f99f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @v1.b(name = "sharedLibraryInfos")
    private static v1.c<List> f100g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @v1.b(name = "privateFlags")
    private static v1.c<Integer> f101h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @v1.b(name = "versionCode")
    private static v1.c<Integer> f102i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @v1.b(name = "longVersionCode")
    private static v1.c<Long> f103j;

    static {
        j.e.r(a.class, ApplicationInfo.class);
    }

    public static String a(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f99f;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void b(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f99f;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static String c(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f96c;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void d(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f96c;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static Integer e(ApplicationInfo applicationInfo) {
        v1.c<Integer> cVar = f101h;
        return Integer.valueOf(cVar != null ? cVar.a(applicationInfo).intValue() : 0);
    }

    public static String f(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f95b;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void g(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f95b;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static String h(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f94a;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void i(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f94a;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static String j(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f97d;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void k(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f97d;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static String l(ApplicationInfo applicationInfo) {
        v1.c<String> cVar = f98e;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void m(ApplicationInfo applicationInfo, String str) {
        v1.c<String> cVar = f98e;
        if (cVar != null) {
            cVar.b(applicationInfo, str);
        }
    }

    public static List n(ApplicationInfo applicationInfo) {
        v1.c<List> cVar = f100g;
        if (cVar != null) {
            return cVar.a(applicationInfo);
        }
        return null;
    }

    public static void o(ApplicationInfo applicationInfo, List list) {
        v1.c<List> cVar = f100g;
        if (cVar != null) {
            cVar.b(applicationInfo, list);
        }
    }

    public static long p(ApplicationInfo applicationInfo) {
        if (f102i != null) {
            return r0.a(applicationInfo).intValue();
        }
        v1.c<Long> cVar = f103j;
        if (cVar != null) {
            return cVar.a(applicationInfo).longValue();
        }
        return -1L;
    }
}
