package f3;

import android.os.Build;
import android.os.Process;
import v1.e;
import v1.f;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static boolean f364a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static boolean f365b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @e(name = "getRuntime", value = {})
    public static h<Object> f366c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @e(name = "setTargetSdkVersion", value = {int.class})
    private static f<Object> f367d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @e(name = "is64Bit", value = {})
    private static f<Boolean> f368e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @e(name = "getInstructionSet", value = {String.class})
    private static h<String> f369f;

    static {
        j.e.q(a.class, "dalvik.system.VMRuntime");
    }

    public static String a(String str) {
        h<String> hVar = f369f;
        if (hVar != null) {
            return hVar.a(new Object[]{str});
        }
        return null;
    }

    public static boolean b(Object obj) {
        Boolean boolA;
        f<Boolean> fVar = f368e;
        if (fVar == null || (boolA = fVar.a(obj, null)) == null) {
            return false;
        }
        return boolA.booleanValue();
    }

    public static boolean c() {
        boolean zB;
        if (f364a) {
            return f365b;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            zB = Process.is64Bit();
        } else {
            h<Object> hVar = f366c;
            zB = b(hVar != null ? hVar.a(null) : null);
        }
        f365b = zB;
        f364a = true;
        return zB;
    }

    public static void d(int i4, Object obj) {
        f<Object> fVar = f367d;
        if (fVar != null) {
            fVar.a(obj, new Object[]{Integer.valueOf(i4)});
        }
    }
}
