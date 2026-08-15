package p;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import v3.j;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HashSet<String> f1182a = new HashSet<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final HashSet<String> f1183b = new HashSet<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final HashSet<String> f1184c = new HashSet<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static String f1185d = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static int f1186e = 0;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static String f1187f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static String f1188g = null;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static String f1189h = null;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static boolean f1190i = false;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final j.a<Integer> f1191j = new j.a<>(new Class[0]);

    public static HashSet<String> a(Context context) {
        String canonicalPath;
        if (!f1190i) {
            HashSet<String> hashSet = f1182a;
            synchronized (hashSet) {
                if (hashSet.isEmpty() && context != null) {
                    String str = context.getApplicationInfo().dataDir;
                    if (!TextUtils.isEmpty(str)) {
                        try {
                            canonicalPath = new File(str).getCanonicalPath();
                        } catch (IOException unused) {
                            canonicalPath = str;
                        }
                        hashSet.add(str);
                        hashSet.add(canonicalPath);
                        String str2 = "/data/data/" + context.getPackageName();
                        String str3 = "/data/user/" + f1186e + "/" + context.getPackageName();
                        hashSet.add(str2);
                        hashSet.add(str3);
                    }
                    canonicalPath = str;
                    hashSet.add(str);
                    hashSet.add(canonicalPath);
                    String str22 = "/data/data/" + context.getPackageName();
                    String str32 = "/data/user/" + f1186e + "/" + context.getPackageName();
                    hashSet.add(str22);
                    hashSet.add(str32);
                }
            }
        }
        return f1182a;
    }

    public static HashSet<String> b(Context context) {
        if (!f1190i) {
            synchronized (f1184c) {
                for (String str : f1183b) {
                    f1184c.add(str + "/Android/data/" + context.getPackageName());
                }
            }
        }
        return f1184c;
    }

    public static HashSet<String> c(Context context) {
        if (!f1190i) {
            HashSet<String> hashSet = f1183b;
            synchronized (hashSet) {
                if (hashSet.isEmpty() && context != null) {
                    try {
                        if (new File("/sdcard").exists()) {
                            hashSet.add("/sdcard");
                        }
                    } catch (Exception unused) {
                    }
                    try {
                        String str = "/storage/emulated/" + f1186e;
                        if (new File(str).exists()) {
                            f1183b.add(str);
                        }
                    } catch (Exception unused2) {
                    }
                    try {
                        f1183b.addAll(d(context));
                    } catch (Exception unused3) {
                    }
                    if (Build.VERSION.SDK_INT > 28) {
                        HashSet<String> hashSet2 = f1183b;
                        hashSet2.add("/sdcard");
                        hashSet2.add("/storage/emulated/" + f1186e);
                    }
                    HashSet hashSet3 = new HashSet();
                    for (String canonicalPath : f1183b) {
                        if (!TextUtils.isEmpty(canonicalPath)) {
                            try {
                                canonicalPath = new File(canonicalPath).getCanonicalPath();
                            } catch (IOException unused4) {
                            }
                        }
                        if (canonicalPath != null) {
                            hashSet3.add(canonicalPath);
                        }
                    }
                    f1183b.addAll(hashSet3);
                }
            }
        }
        return f1183b;
    }

    public static ArrayList d(Context context) {
        HashSet hashSet = new HashSet();
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            Method declaredMethod = StorageManager.class.getDeclaredMethod("getVolumePaths", new Class[0]);
            declaredMethod.setAccessible(true);
            String[] strArr = (String[]) declaredMethod.invoke(storageManager, new Object[0]);
            if (strArr != null && strArr.length > 0) {
                for (String str : strArr) {
                    hashSet.add(str);
                }
            }
        } catch (Exception unused) {
        }
        return new ArrayList(hashSet);
    }

    public static String e() {
        String str = f1188g;
        if (str != null) {
            return str;
        }
        HashSet<String> hashSet = f1182a;
        if (!hashSet.isEmpty()) {
            for (String str2 : hashSet) {
                if (str2.startsWith("/data")) {
                    f1188g = str2;
                    return str2;
                }
            }
        } else if (!TextUtils.isEmpty(f1185d)) {
            String str3 = "/data/data/" + f1185d;
            f1188g = str3;
            return str3;
        }
        return f1188g;
    }

    public static String f() {
        String str = f1189h;
        if (str != null) {
            return str;
        }
        if (!TextUtils.isEmpty(f1185d)) {
            StringBuilder sb = new StringBuilder();
            String str2 = f1187f;
            if (str2 == null) {
                HashSet<String> hashSet = f1183b;
                if (hashSet.isEmpty() || hashSet.contains("/sdcard")) {
                    f1187f = "/sdcard";
                    str2 = "/sdcard";
                } else {
                    Iterator<String> it = hashSet.iterator();
                    String next = null;
                    while (it.hasNext()) {
                        next = it.next();
                        if (next.startsWith("/sdcard")) {
                            break;
                        }
                    }
                    f1187f = next;
                    str2 = next;
                }
            }
            sb.append(str2);
            sb.append("/Android/data/");
            sb.append(f1185d);
            f1189h = sb.toString();
        }
        return f1189h;
    }

    public static void g(Context context) {
        File externalCacheDir;
        boolean z3;
        f1188g = context.getApplicationInfo().dataDir;
        try {
            externalCacheDir = context.getExternalCacheDir();
        } catch (Exception unused) {
            externalCacheDir = null;
        }
        if (externalCacheDir != null) {
            String parent = externalCacheDir.getParent();
            f1189h = parent;
            if (parent != null) {
                Iterator<String> it = c(context).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z3 = false;
                        break;
                    }
                    String next = it.next();
                    if (f1189h.startsWith(next)) {
                        f1187f = next;
                        z3 = true;
                        break;
                    }
                }
                if (z3) {
                    return;
                }
                f1189h = null;
            }
        }
    }
}
