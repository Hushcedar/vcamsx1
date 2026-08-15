package u1;

import androidx.core.os.perationCompat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HashSet<String> f1670a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final HashSet<String> f1671b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final HashSet<String> f1672c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final HashSet<String> f1673d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final HashSet<String> f1674e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final HashSet<String> f1675f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final HashSet<String> f1676g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final HashMap<String, HashSet<String>> f1677h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final HashSet<String> f1678i;

    static {
        perationCompat.init0(232);
        HashSet<String> hashSet = new HashSet<>();
        f1670a = hashSet;
        HashSet<String> hashSet2 = new HashSet<>();
        f1671b = hashSet2;
        HashSet<String> hashSet3 = new HashSet<>();
        f1672c = hashSet3;
        HashSet<String> hashSet4 = new HashSet<>();
        f1673d = hashSet4;
        HashSet<String> hashSet5 = new HashSet<>();
        f1674e = hashSet5;
        HashSet<String> hashSet6 = new HashSet<>();
        f1675f = hashSet6;
        HashSet<String> hashSet7 = new HashSet<>();
        f1676g = hashSet7;
        HashMap<String, HashSet<String>> map = new HashMap<>();
        f1677h = map;
        f1678i = new HashSet<>();
        hashSet.addAll(Arrays.asList("com.google.android.gms", "com.google.android.gsf", "com.android.vending"));
        hashSet2.addAll(Arrays.asList("com.android.chrome", "com.google.android.googlequicksearchbox", "com.google.android.gm", "com.google.android.apps.maps", "com.google.android.youtube", "com.google.android.apps.docs", "com.google.android.music", "com.google.android.apps.tachyon"));
        hashSet3.addAll(Arrays.asList("android.permission.WRITE_SETTINGS", "android.permission.GET_ACCOUNTS", "android.permission.GET_ACCOUNTS_PRIVILEGED", "android.permission.READ_DEVICE_CONFIG", "android.permission.BACKUP", "android.permission.SEND_SMS"));
        hashSet4.addAll(Arrays.asList("android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS", "android.permission.READ_CELL_BROADCASTS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.PROCESS_OUTGOING_CALLS"));
        hashSet5.addAll(Arrays.asList("com.zhiliaoapp.musically", "com.ss.android.ugc.trill"));
        hashSet6.addAll(Arrays.asList("com.google.android.gms.auth.uiflows.common.UnpackingRedirectActivity"));
        hashSet7.addAll(Arrays.asList("com.google.android.youtube/.api.service.YouTubeService"));
        map.put("com.google.android.apps.translate", hashSet);
    }

    public static native boolean a(String str);

    public static native boolean b(String str, String str2);

    public static native boolean c(String str);
}
