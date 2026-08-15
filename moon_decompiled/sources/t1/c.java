package t1;

import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final boolean f1560a = "eng".equals(Build.TYPE);

    public static boolean a() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean b() {
        return Build.VERSION.SDK_INT >= 29;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean c() {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 33
            if (r0 > r1) goto L15
            r2 = 0
            if (r0 != r1) goto L16
            r1 = 23
            if (r0 < r1) goto L12
            int r0 = b0.b.a()     // Catch: java.lang.Throwable -> L12
            goto L13
        L12:
            r0 = 0
        L13:
            if (r0 <= 0) goto L16
        L15:
            r2 = 1
        L16:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: t1.c.c():boolean");
    }

    public static boolean d() {
        if (Build.DISPLAY.toUpperCase().startsWith("EMUI")) {
            return true;
        }
        v1.h<String> hVar = k2.k.f702b;
        String strA = hVar != null ? hVar.a(new Object[]{"ro.build.version.emui", null}) : null;
        return strA != null && strA.contains("EmotionUI");
    }
}
