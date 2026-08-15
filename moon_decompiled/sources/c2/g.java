package c2;

import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.net.Uri;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mode")
    private static v1.c<Integer> f122a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "installFlags")
    private static v1.c<Integer> f123b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "installLocation")
    private static v1.c<Integer> f124c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "sizeBytes")
    private static v1.c<Long> f125d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.b(name = "appIconLastModified")
    private static v1.c<Long> f126e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.b(name = "appPackageName")
    private static v1.c<String> f127f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @v1.b(name = "abiOverride")
    private static v1.c<String> f128g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @v1.b(name = "volumeUuid")
    private static v1.c<String> f129h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @v1.b(name = "appIcon")
    private static v1.c<Bitmap> f130i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @v1.b(name = "appLabel")
    private static v1.c<CharSequence> f131j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @v1.b(name = "originatingUri")
    private static v1.c<Uri> f132k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @v1.b(name = "referrerUri")
    private static v1.c<Uri> f133l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @v1.b(name = "originatingUid")
    private static v1.c<Integer> f134m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    @v1.b(name = "installReason")
    private static v1.c<Integer> f135n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    @v1.b(name = "isMultiPackage")
    private static v1.c<Boolean> f136o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    @v1.b(name = "dataLoaderParams")
    private static v1.c<Object> f137p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    @v1.b(name = "grantedRuntimePermissions")
    private static v1.c<String[]> f138q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    @v1.b(name = "whitelistedRestrictedPermissions")
    private static v1.c<List> f139r;

    static {
        j.e.r(g.class, PackageInstaller.SessionParams.class);
    }

    public static String a(PackageInstaller.SessionParams sessionParams) {
        v1.c<String> cVar = f128g;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static void b(PackageInstaller.SessionParams sessionParams, String str) {
        v1.c<String> cVar = f128g;
        if (cVar != null) {
            cVar.b(sessionParams, str);
        }
    }

    public static Bitmap c(PackageInstaller.SessionParams sessionParams) {
        v1.c<Bitmap> cVar = f130i;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static long d(PackageInstaller.SessionParams sessionParams) {
        v1.c<Long> cVar = f126e;
        if (cVar != null) {
            return cVar.a(sessionParams).longValue();
        }
        return -1L;
    }

    public static void e(PackageInstaller.SessionParams sessionParams, long j4) {
        v1.c<Long> cVar = f126e;
        if (cVar != null) {
            cVar.b(sessionParams, Long.valueOf(j4));
        }
    }

    public static CharSequence f(PackageInstaller.SessionParams sessionParams) {
        v1.c<CharSequence> cVar = f131j;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static String g(Object obj) {
        v1.c<String> cVar = f127f;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }

    public static Object h(PackageInstaller.SessionParams sessionParams) {
        v1.c<Object> cVar = f137p;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static void i(PackageInstaller.SessionParams sessionParams, String[] strArr) {
        v1.c<String[]> cVar = f138q;
        if (cVar != null) {
            cVar.b(sessionParams, strArr);
        }
    }

    public static String[] j(PackageInstaller.SessionParams sessionParams) {
        v1.c<String[]> cVar = f138q;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static int k(Object obj) {
        v1.c<Integer> cVar = f123b;
        if (cVar != null) {
            return cVar.a(obj).intValue();
        }
        return 0;
    }

    public static void l(int i4, Object obj) {
        v1.c<Integer> cVar = f123b;
        if (cVar != null) {
            cVar.b(obj, Integer.valueOf(i4));
        }
    }

    public static int m(PackageInstaller.SessionParams sessionParams) {
        v1.c<Integer> cVar = f124c;
        if (cVar != null) {
            return cVar.a(sessionParams).intValue();
        }
        return 0;
    }

    public static int n(PackageInstaller.SessionParams sessionParams) {
        v1.c<Integer> cVar = f135n;
        return (cVar != null ? cVar.a(sessionParams) : null).intValue();
    }

    public static boolean o(Object obj) {
        v1.c<Boolean> cVar = f136o;
        if (cVar != null) {
            return cVar.a(obj).booleanValue();
        }
        return false;
    }

    public static int p(Object obj) {
        v1.c<Integer> cVar = f122a;
        if (cVar != null) {
            return cVar.a(obj).intValue();
        }
        return -1;
    }

    public static int q(PackageInstaller.SessionParams sessionParams) {
        v1.c<Integer> cVar = f134m;
        return (cVar != null ? cVar.a(sessionParams) : null).intValue();
    }

    public static Uri r(PackageInstaller.SessionParams sessionParams) {
        v1.c<Uri> cVar = f132k;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static Uri s(PackageInstaller.SessionParams sessionParams) {
        v1.c<Uri> cVar = f133l;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static long t(PackageInstaller.SessionParams sessionParams) {
        v1.c<Long> cVar = f125d;
        if (cVar != null) {
            return cVar.a(sessionParams).longValue();
        }
        return 0L;
    }

    public static String u(PackageInstaller.SessionParams sessionParams) {
        v1.c<String> cVar = f129h;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }

    public static void v(PackageInstaller.SessionParams sessionParams, String str) {
        v1.c<String> cVar = f129h;
        if (cVar != null) {
            cVar.b(sessionParams, str);
        }
    }

    public static List w(PackageInstaller.SessionParams sessionParams) {
        v1.c<List> cVar = f139r;
        if (cVar != null) {
            return cVar.a(sessionParams);
        }
        return null;
    }
}
