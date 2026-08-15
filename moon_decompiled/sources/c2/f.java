package c2;

import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "sessionId")
    private static v1.c<Integer> f109a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "installerPackageName")
    private static v1.c<String> f110b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "resolvedBaseCodePath")
    private static v1.c<String> f111c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "progress")
    private static v1.c<Float> f112d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.b(name = "sealed")
    private static v1.c<Boolean> f113e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.b(name = "active")
    private static v1.c<Boolean> f114f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @v1.b(name = "mode")
    private static v1.c<Integer> f115g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @v1.b(name = "sizeBytes")
    private static v1.c<Long> f116h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @v1.b(name = "appPackageName")
    private static v1.c<String> f117i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @v1.b(name = "appIcon")
    private static v1.c<Bitmap> f118j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @v1.b(name = "appLabel")
    private static v1.c<CharSequence> f119k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @v1.b(name = "parentSessionId")
    private static v1.c<Integer> f120l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @v1.e(name = "", value = {})
    private static v1.a<PackageInstaller.SessionInfo> f121m;

    static {
        j.e.r(f.class, PackageInstaller.SessionInfo.class);
    }

    public static void a(PackageInstaller.SessionInfo sessionInfo, boolean z3) {
        v1.c<Boolean> cVar = f114f;
        if (cVar != null) {
            cVar.b(sessionInfo, Boolean.valueOf(z3));
        }
    }

    public static void b(PackageInstaller.SessionInfo sessionInfo, Bitmap bitmap) {
        v1.c<Bitmap> cVar = f118j;
        if (cVar != null) {
            cVar.b(sessionInfo, bitmap);
        }
    }

    public static void c(PackageInstaller.SessionInfo sessionInfo, CharSequence charSequence) {
        v1.c<CharSequence> cVar = f119k;
        if (cVar != null) {
            cVar.b(sessionInfo, charSequence);
        }
    }

    public static void d(PackageInstaller.SessionInfo sessionInfo, String str) {
        v1.c<String> cVar = f117i;
        if (cVar != null) {
            cVar.b(sessionInfo, str);
        }
    }

    public static void e(PackageInstaller.SessionInfo sessionInfo, String str) {
        v1.c<String> cVar = f110b;
        if (cVar != null) {
            cVar.b(sessionInfo, str);
        }
    }

    public static void f(int i4, PackageInstaller.SessionInfo sessionInfo) {
        v1.c<Integer> cVar = f115g;
        if (cVar != null) {
            cVar.b(sessionInfo, Integer.valueOf(i4));
        }
    }

    public static PackageInstaller.SessionInfo g() {
        v1.a<PackageInstaller.SessionInfo> aVar = f121m;
        if (aVar != null) {
            return aVar.a(null);
        }
        return null;
    }

    public static void h(int i4, PackageInstaller.SessionInfo sessionInfo) {
        v1.c<Integer> cVar = f120l;
        if (cVar != null) {
            cVar.b(sessionInfo, Integer.valueOf(i4));
        }
    }

    public static void i(PackageInstaller.SessionInfo sessionInfo, float f4) {
        v1.c<Float> cVar = f112d;
        if (cVar != null) {
            cVar.b(sessionInfo, Float.valueOf(f4));
        }
    }

    public static void j(PackageInstaller.SessionInfo sessionInfo, String str) {
        v1.c<String> cVar = f111c;
        if (cVar != null) {
            cVar.b(sessionInfo, str);
        }
    }

    public static void k(PackageInstaller.SessionInfo sessionInfo, boolean z3) {
        v1.c<Boolean> cVar = f113e;
        if (cVar != null) {
            cVar.b(sessionInfo, Boolean.valueOf(z3));
        }
    }

    public static void l(int i4, PackageInstaller.SessionInfo sessionInfo) {
        v1.c<Integer> cVar = f109a;
        if (cVar != null) {
            cVar.b(sessionInfo, Integer.valueOf(i4));
        }
    }

    public static void m(PackageInstaller.SessionInfo sessionInfo, long j4) {
        v1.c<Long> cVar = f116h;
        if (cVar != null) {
            cVar.b(sessionInfo, Long.valueOf(j4));
        }
    }
}
