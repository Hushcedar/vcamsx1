package c2;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "", value = {})
    private static v1.a<Object> f146a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "parsePackage", value = {File.class, int.class})
    private static v1.f<Object> f147b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.d(name = "setCallback", value = {@v1.i(strings = {"android.content.pm.PackageParser$Callback"}, type = 1)})
    private static v1.f<Object> f148c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.d(name = "collectCertificates", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.f<Object> f149d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.d(name = "collectCertificates", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {boolean.class})})
    private static v1.f<Object> f150e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @v1.d(name = "generatePackageInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int[].class, int.class, long.class, long.class, HashSet.class}), @v1.i(strings = {"android.content.pm.PackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f151f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @v1.d(name = "generatePackageInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int[].class, int.class, long.class, long.class}), @v1.i(strings = {"android.util.ArraySet", "android.content.pm.PackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f152g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @v1.d(name = "generatePackageInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int[].class, int.class, long.class, long.class, Set.class}), @v1.i(strings = {"android.content.pm.PackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f153h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @v1.d(name = "generatePackageInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int[].class, int.class, long.class, long.class, Set.class}), @v1.i(strings = {"android.content.pm.pkg.FrameworkPackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f154i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @v1.d(name = "generateApplicationInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int.class}), @v1.i(strings = {"android.content.pm.PackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f155j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @v1.d(name = "generateApplicationInfo", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1), @v1.i(classes = {int.class}), @v1.i(strings = {"android.content.pm.pkg.FrameworkPackageUserState"}, type = 1), @v1.i(classes = {int.class})})
    private static v1.h<Object> f156k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @v1.e(name = "parsePackageLite", value = {File.class, int.class})
    private static v1.h<Object> f157l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @v1.e(name = "parseApkLite", value = {File.class, int.class})
    private static v1.h<Object> f158m;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f159a = j.e.q(a.class, "android.content.pm.PackageParser$Activity");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<Object> f160b;
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "splitName")
        private static v1.c<String> f161a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "packageName")
        private static v1.c<String> f162b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "versionCode")
        private static v1.c<Integer> f163c;

        static {
            j.e.q(b.class, "android.content.pm.PackageParser$ApkLite");
        }

        public static String a(Object obj) {
            v1.c<String> cVar = f162b;
            if (cVar != null) {
                return (String) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static String b(Object obj) {
            v1.c<String> cVar = f161a;
            if (cVar != null) {
                return (String) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static void c(Object obj) {
            v1.c<Integer> cVar = f163c;
            Integer num = cVar != null ? (Integer) cVar.f1714a.get(obj) : null;
            if (Integer.class.isInstance(num)) {
                num.intValue();
            }
        }
    }

    @TargetApi(26)
    public static final class c implements PackageParser.Callback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final PackageManager f164a;

        public c(PackageManager packageManager) {
            this.f164a = packageManager;
        }

        public String[] getOverlayApks(String str) {
            return null;
        }

        public String[] getOverlayPaths(String str, String str2) {
            return null;
        }

        public boolean hasFeature(String str) {
            return this.f164a.hasSystemFeature(str);
        }
    }

    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "className")
        public static v1.c<String> f165a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "intents")
        public static v1.c<List> f166b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "metaData")
        public static v1.c<Bundle> f167c;

        static {
            j.e.q(d.class, "android.content.pm.PackageParser$Component");
        }
    }

    public static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "hasDefault")
        public static v1.c<Boolean> f168a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "labelRes")
        public static v1.c<Integer> f169b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "nonLocalizedLabel")
        public static v1.c<CharSequence> f170c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "icon")
        public static v1.c<Integer> f171d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @v1.b(name = "logo")
        public static v1.c<Integer> f172e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        @v1.b(name = "banner")
        public static v1.c<Integer> f173f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        @v1.b(name = "preferred")
        public static v1.c<Integer> f174g;

        static {
            j.e.q(e.class, "android.content.pm.PackageParser$IntentInfo");
        }
    }

    public static class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "packageName")
        private static v1.c<String> f175a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "baseCodePath")
        private static v1.c<String> f176b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "splitCodePaths")
        private static v1.c<String[]> f177c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "use32bitAbi")
        private static v1.c<Boolean> f178d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @v1.b(name = "versionCode")
        private static v1.c<Integer> f179e;

        static {
            j.e.q(f.class, "android.content.pm.PackageParser$PackageLite");
        }

        public static String a(Object obj) {
            v1.c<String> cVar = f176b;
            if (cVar != null) {
                return (String) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static String b(Object obj) {
            v1.c<String> cVar = f175a;
            if (cVar != null) {
                return (String) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static String[] c(Object obj) {
            v1.c<String[]> cVar = f177c;
            if (cVar != null) {
                return (String[]) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static boolean d(Object obj) {
            v1.c<Boolean> cVar = f178d;
            if (cVar != null) {
                return ((Boolean) cVar.f1714a.get(obj)).booleanValue();
            }
            return false;
        }

        public static int e(Object obj) {
            v1.c<Integer> cVar = f179e;
            if (cVar != null) {
                return ((Integer) cVar.f1714a.get(obj)).intValue();
            }
            return 0;
        }
    }

    public static class g {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "activities")
        public static v1.c<List> f180a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "receivers")
        public static v1.c<List> f181b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "services")
        public static v1.c<List> f182c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "providers")
        public static v1.c<List> f183d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @v1.b(name = "permissions")
        public static v1.c<List> f184e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        @v1.b(name = "permissionGroups")
        public static v1.c<List> f185f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        @v1.b(name = "requestedPermissions")
        public static v1.c<List> f186g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        @v1.b(name = "instrumentation")
        public static v1.c<List> f187h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        @v1.b(name = "packageName")
        public static v1.c<String> f188i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        @v1.b(name = "mSharedUserId")
        public static v1.c<String> f189j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        @v1.b(name = "staticSharedLibName")
        public static v1.c<String> f190k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        @v1.b(name = "staticSharedLibVersion")
        public static v1.c<Long> f191l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        @v1.b(name = "staticSharedLibVersion")
        public static v1.c<Integer> f192m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        @v1.b(name = "usesLibraries")
        public static v1.c<List> f193n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        @v1.b(name = "usesStaticLibraries")
        private static v1.c<List> f194o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        @v1.b(name = "usesStaticLibrariesVersions")
        private static v1.c<long[]> f195p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        @v1.b(name = "usesOptionalLibraries")
        public static v1.c<List> f196q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        @v1.b(name = "mAppMetaData")
        public static v1.c<Bundle> f197r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        @v1.b(name = "splitNames")
        private static v1.c<String[]> f198s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        @v1.b(name = "splitCodePaths")
        private static v1.c<String[]> f199t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        @v1.b(name = "mPreferredOrder")
        private static v1.c<Integer> f200u;

        static {
            j.e.q(g.class, "android.content.pm.PackageParser$Package");
        }

        public static Integer a(Object obj) {
            v1.c<Integer> cVar = f200u;
            if (cVar != null) {
                return (Integer) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static String[] b(Object obj) {
            v1.c<String[]> cVar = f199t;
            if (cVar != null) {
                return (String[]) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static String[] c(Object obj) {
            v1.c<String[]> cVar = f198s;
            if (cVar != null) {
                return (String[]) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static List d(Object obj) {
            v1.c<List> cVar = f194o;
            if (cVar != null) {
                return (List) cVar.f1714a.get(obj);
            }
            return null;
        }

        public static long[] e(Object obj) {
            v1.c<long[]> cVar = f195p;
            if (cVar != null) {
                return (long[]) cVar.f1714a.get(obj);
            }
            return null;
        }
    }

    public static class h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f201a = j.e.q(h.class, "android.content.pm.PackageParser$PermissionGroup");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<PermissionGroupInfo> f202b;
    }

    /* JADX INFO: renamed from: c2.i$i, reason: collision with other inner class name */
    public static class C0009i {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f203a = j.e.q(C0009i.class, "android.content.pm.PackageParser$Permission");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<PermissionInfo> f204b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "group")
        public static v1.c<Object> f205c;
    }

    public static class j {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f206a = j.e.q(j.class, "android.content.pm.PackageParser$Provider");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<Object> f207b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "syncable")
        public static v1.c<Boolean> f208c;
    }

    public static class k {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class f209a = j.e.q(k.class, "android.content.pm.PackageParser$Service");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "info")
        public static v1.c<Object> f210b;
    }

    static {
        j.e.q(i.class, "android.content.pm.PackageParser");
    }

    public static void a(Object obj, Object obj2, int i4) {
        v1.f<Object> fVar = f150e;
        if (fVar != null) {
            fVar.f1715a.invoke(obj, obj2, Boolean.valueOf((i4 & 64) != 0));
            return;
        }
        v1.f<Object> fVar2 = f149d;
        if (fVar2 != null) {
            fVar2.f1715a.invoke(obj, obj2, Integer.valueOf(i4));
        }
    }

    public static Object b(Object obj, int i4, Object obj2, int i5) {
        v1.h<Object> hVar = f156k;
        if (hVar != null) {
            return hVar.b(new Object[]{obj, Integer.valueOf(i4), obj2, Integer.valueOf(i5)});
        }
        v1.h<Object> hVar2 = f155j;
        if (hVar2 != null) {
            return hVar2.b(new Object[]{obj, Integer.valueOf(i4), obj2, Integer.valueOf(i5)});
        }
        return null;
    }

    public static Object c(Object obj, int[] iArr, int i4, long j4, long j5, HashSet<String> hashSet, Object obj2, int i5) {
        v1.h<Object> hVar = f154i;
        if (hVar != null) {
            return hVar.b(new Object[]{obj, iArr, Integer.valueOf(i4), Long.valueOf(j4), Long.valueOf(j5), hashSet, obj2, Integer.valueOf(i5)});
        }
        v1.h<Object> hVar2 = f153h;
        if (hVar2 != null) {
            return hVar2.b(new Object[]{obj, iArr, Integer.valueOf(i4), Long.valueOf(j4), Long.valueOf(j5), hashSet, obj2, Integer.valueOf(i5)});
        }
        if (f152g != null) {
            return f152g.b(new Object[]{obj, iArr, Integer.valueOf(i4), Long.valueOf(j4), Long.valueOf(j5), s2.a.a(hashSet), obj2, Integer.valueOf(i5)});
        }
        v1.h<Object> hVar3 = f151f;
        if (hVar3 != null) {
            return hVar3.b(new Object[]{obj, iArr, Integer.valueOf(i4), Long.valueOf(j4), Long.valueOf(j5), hashSet, obj2, Integer.valueOf(i5)});
        }
        return null;
    }

    public static Object d() {
        v1.a<Object> aVar = f146a;
        if (aVar != null) {
            return aVar.a(null);
        }
        return null;
    }

    public static Object e(File file) {
        v1.h<Object> hVar = f158m;
        if (hVar != null) {
            return hVar.b(new Object[]{file, 256});
        }
        return null;
    }

    public static Object f(Object obj, File file) {
        v1.f<Object> fVar = f147b;
        if (fVar != null) {
            return fVar.f1715a.invoke(obj, file, 0);
        }
        return null;
    }

    public static Object g(File file) {
        v1.h<Object> hVar = f157l;
        if (hVar != null) {
            return hVar.b(new Object[]{file, 0});
        }
        return null;
    }

    public static void h(Object obj, c cVar) {
        v1.f<Object> fVar = f148c;
        if (fVar != null) {
            fVar.a(obj, new Object[]{cVar});
        }
    }
}
