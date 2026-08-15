package c2;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import x1.t;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "disablePackageInfoCache", value = {})
    public static v1.h<Object> f140a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "getPackageInfoAsUserUncached", value = {String.class, int.class, int.class})
    public static v1.h<PackageInfo> f141b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "getPackageInfoAsUserUncached", value = {String.class, long.class, int.class})
    public static v1.h<PackageInfo> f142c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.e(name = "getApplicationInfoAsUserUncached", value = {String.class, int.class, int.class})
    public static v1.h<ApplicationInfo> f143d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.e(name = "getApplicationInfoAsUserUncached", value = {String.class, long.class, int.class})
    public static v1.h<ApplicationInfo> f144e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.b(name = "sPackageInfoCache")
    private static v1.g<Object> f145f;

    static {
        j.e.q(h.class, "android.content.pm.PackageManager");
    }

    public static void a() {
        boolean z3;
        v1.h<Object> hVar;
        Object objA;
        v1.g<Object> gVar = f145f;
        if (gVar == null || (objA = gVar.a()) == null) {
            z3 = false;
        } else {
            t.a(objA);
            z3 = true;
        }
        if (z3 || (hVar = f140a) == null) {
            return;
        }
        hVar.a(new Object[0]);
    }

    public static ApplicationInfo b(int i4, PackageManager packageManager, String str) {
        ApplicationInfo applicationInfoA;
        v1.h<ApplicationInfo> hVar = f144e;
        if (hVar != null) {
            applicationInfoA = hVar.a(new Object[]{str, Integer.valueOf(i4), Integer.valueOf(k2.l.b())});
        } else {
            v1.h<ApplicationInfo> hVar2 = f143d;
            if (hVar2 == null) {
                try {
                    return packageManager.getApplicationInfo(str, i4);
                } catch (Exception unused) {
                    return null;
                }
            }
            applicationInfoA = hVar2.a(new Object[]{str, Integer.valueOf(i4), Integer.valueOf(k2.l.b())});
        }
        return applicationInfoA;
    }

    public static PackageInfo c(int i4, PackageManager packageManager, String str) {
        PackageInfo packageInfoA;
        v1.h<PackageInfo> hVar = f142c;
        if (hVar != null) {
            packageInfoA = hVar.a(new Object[]{str, Integer.valueOf(i4), Integer.valueOf(k2.l.b())});
        } else {
            v1.h<PackageInfo> hVar2 = f141b;
            if (hVar2 == null) {
                try {
                    return packageManager.getPackageInfo(str, i4);
                } catch (Exception unused) {
                    return null;
                }
            }
            packageInfoA = hVar2.a(new Object[]{str, Integer.valueOf(i4), Integer.valueOf(k2.l.b())});
        }
        return packageInfoA;
    }
}
