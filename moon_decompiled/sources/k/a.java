package k;

import java.io.File;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HashSet<String> f673a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f674b;

    static {
        HashSet<String> hashSet = new HashSet<>();
        f673a = hashSet;
        hashSet.add("com.google.android.gms");
        hashSet.add("com.google.android.gsf");
        hashSet.add("com.android.vending");
        f674b = new File("/system/framework/org.apache.http.legacy.boot.jar").exists() ? "/system/framework/org.apache.http.legacy.boot.jar" : "/system/framework/org.apache.http.legacy.jar";
    }

    public static boolean a(String str) {
        return f673a.contains(str);
    }
}
