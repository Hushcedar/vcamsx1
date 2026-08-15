package v3;

import android.os.Process;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f1733a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static int f1734b = 1;

    public static String a(int i4) {
        BufferedReader bufferedReader;
        if (i4 == -1) {
            i4 = Process.myPid();
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + i4 + "/cmdline"), "iso-8859-1"));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int i5 = bufferedReader.read();
                    if (i5 <= 0) {
                        break;
                    }
                    sb.append((char) i5);
                }
                String string = sb.toString();
                try {
                    bufferedReader.close();
                } catch (Exception unused) {
                }
                return string;
            } catch (Throwable unused2) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception unused3) {
                    }
                }
                return null;
            }
        } catch (Throwable unused4) {
            bufferedReader = null;
        }
    }

    public static boolean b() {
        return f1734b == 3;
    }

    public static boolean c() {
        return f1734b == 2;
    }

    public static boolean d() {
        return f1734b == 5;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0094  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void e(android.content.Context r4) {
        /*
            int r0 = v3.i.f1734b
            r1 = 1
            if (r0 != r1) goto L97
            java.lang.String r0 = r4.getPackageName()
            java.lang.String r1 = v3.i.f1733a
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L12
            goto L42
        L12:
            int r1 = android.os.Process.myPid()     // Catch: java.lang.Exception -> L3b
            java.lang.String r2 = "activity"
            java.lang.Object r4 = r4.getSystemService(r2)     // Catch: java.lang.Exception -> L3b
            android.app.ActivityManager r4 = (android.app.ActivityManager) r4     // Catch: java.lang.Exception -> L3b
            java.util.List r4 = r4.getRunningAppProcesses()     // Catch: java.lang.Exception -> L3b
            java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Exception -> L3b
        L26:
            boolean r2 = r4.hasNext()     // Catch: java.lang.Exception -> L3b
            if (r2 == 0) goto L3b
            java.lang.Object r2 = r4.next()     // Catch: java.lang.Exception -> L3b
            android.app.ActivityManager$RunningAppProcessInfo r2 = (android.app.ActivityManager.RunningAppProcessInfo) r2     // Catch: java.lang.Exception -> L3b
            int r3 = r2.pid     // Catch: java.lang.Exception -> L3b
            if (r3 != r1) goto L26
            java.lang.String r1 = r2.processName     // Catch: java.lang.Exception -> L3b
            v3.i.f1733a = r1     // Catch: java.lang.Exception -> L3b
            goto L42
        L3b:
            r4 = -1
            java.lang.String r1 = a(r4)
            v3.i.f1733a = r1
        L42:
            boolean r4 = android.text.TextUtils.equals(r1, r0)
            if (r4 != 0) goto L94
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r2 = ":client"
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            boolean r4 = android.text.TextUtils.equals(r1, r4)
            if (r4 == 0) goto L60
            goto L94
        L60:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r2 = ":core"
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            boolean r4 = android.text.TextUtils.equals(r1, r4)
            if (r4 == 0) goto L79
            r4 = 5
            goto L95
        L79:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r0 = ":assist"
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            boolean r4 = android.text.TextUtils.equals(r1, r4)
            if (r4 == 0) goto L92
            r4 = 4
            goto L95
        L92:
            r4 = 3
            goto L95
        L94:
            r4 = 2
        L95:
            v3.i.f1734b = r4
        L97:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: v3.i.e(android.content.Context):void");
    }
}
