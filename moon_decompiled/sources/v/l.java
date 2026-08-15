package v;

import android.os.Bundle;
import androidx.core.os.perationCompat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final List<String> f1709a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final HashSet f1710b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final HashMap f1711c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final HashSet f1712d;

    static {
        perationCompat.init0(199);
        f1709a = Arrays.asList("textclassifier", "runtime");
        HashSet hashSet = new HashSet();
        f1710b = hashSet;
        HashMap map = new HashMap();
        f1711c = map;
        map.put("user_setup_complete", "1");
        map.put("install_non_market_apps", "1");
        hashSet.add("gearhead:driving_mode_settings_enabled");
        HashSet hashSet2 = new HashSet();
        f1712d = hashSet2;
        hashSet2.add("download_manager_recommended_max_bytes_over_mobile");
        hashSet2.add("sqlite_compatibility_wal_flags");
        hashSet2.add("device_provisioned");
        hashSet2.add("location_providers_allowed");
    }

    public static native Bundle a(String str, String str2);

    public static native int b(String str);
}
