package u1;

import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.os.perationCompat;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1649c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final HashSet f1650a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final HashSet f1651b;

    public class a extends v3.k<e> {
        static {
            perationCompat.init0(732);
        }

        @Override // v3.k
        public final native e a();
    }

    static {
        perationCompat.init0(224);
        f1649c = new a();
    }

    public e() {
        HashSet hashSet = new HashSet();
        this.f1650a = hashSet;
        HashSet hashSet2 = new HashSet();
        this.f1651b = hashSet2;
        hashSet.add("android.net.conn.CONNECTIVITY_CHANGE");
        hashSet.add("android.net.conn.CAPTIVE_PORTAL");
        hashSet.add("android.net.wifi.NETWORK_IDS_CHANGED");
        hashSet.add("android.net.wifi.STATE_CHANGE");
        hashSet.add("android.net.wifi.RSSI_CHANGED");
        hashSet.add("android.net.wifi.SCAN_RESULTS");
        hashSet.add("android.net.wifi.WIFI_STATE_CHANGED");
        hashSet.add("android.net.wifi.action.WIFI_SCAN_AVAILABILITY_CHANGED");
        hashSet.add("android.intent.action.HEADSET_PLUG");
        hashSet.add("android.intent.action.SCREEN_OFF");
        hashSet.add("android.intent.action.SCREEN_ON");
        hashSet.add("android.intent.action.DREAMING_STOPPED");
        hashSet.add("android.intent.action.DREAMING_STARTED");
        hashSet.add("android.intent.action.DEVICE_STORAGE_OK");
        hashSet.add("android.intent.action.DEVICE_STORAGE_LOW");
        hashSet.add("android.intent.action.TIME_TICK");
        hashSet.add("android.intent.action.TIME_SET");
        hashSet.add("android.intent.action.DATE_CHANGED");
        hashSet.add("android.intent.action.TIMEZONE_CHANGED");
        hashSet.add("android.intent.action.CLEAR_DNS_CACHE");
        hashSet.add("android.intent.action.ALARM_CHANGED");
        hashSet.add("android.intent.action.DATE_CHANGED");
        hashSet.add("android.intent.action.TIMEZONE_CHANGED");
        hashSet.add("android.intent.action.BATTERY_CHANGED");
        hashSet.add("android.intent.action.BATTERY_LOW");
        hashSet.add("android.intent.action.BATTERY_OKAY");
        hashSet.add("android.intent.action.ACTION_POWER_CONNECTED");
        hashSet.add("android.intent.action.ACTION_POWER_DISCONNECTED");
        hashSet.add("android.intent.action.CONFIGURATION_CHANGED");
        hashSet.add("android.intent.action.SPLIT_CONFIGURATION_CHANGED");
        hashSet.add("android.intent.action.LOCALE_CHANGED");
        hashSet.add("android.intent.action.DEVICE_STORAGE_LOW");
        hashSet.add("android.intent.action.DEVICE_STORAGE_OK");
        hashSet.add("android.intent.action.MANAGE_PACKAGE_STORAGE");
        hashSet.add("android.intent.action.MEDIA_SCANNER_STARTED");
        hashSet.add("android.intent.action.MEDIA_SCANNER_FINISHED");
        hashSet.add("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        hashSet.add("android.intent.action.MEDIA_BUTTON");
        hashSet.add("android.intent.action.CAMERA_BUTTON");
        hashSet.add("android.intent.action.GTALK_CONNECTED");
        hashSet.add("android.intent.action.GTALK_DISCONNECTED");
        hashSet.add("android.intent.action.INPUT_METHOD_CHANGED");
        hashSet.add("android.intent.action.USER_BACKGROUND");
        hashSet.add("android.intent.action.USER_FOREGROUND");
        hashSet.add("android.location.MODE_CHANGED");
        hashSet.add("android.location.PROVIDERS_CHANGED");
        hashSet.add("android.intent.action.DOWNLOAD_COMPLETE");
        hashSet.add("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
        hashSet2.add("android.intent.action.LOCKED_BOOT_COMPLETED");
        hashSet2.add("android.intent.action.BOOT_COMPLETED");
        hashSet2.add("android.intent.action.ACTION_SHUTDOWN");
        hashSet2.add("android.intent.action.UMS_CONNECTED");
        hashSet2.add("android.intent.action.UMS_DISCONNECTED");
        hashSet2.add("android.intent.action.MEDIA_REMOVED");
        hashSet2.add("android.intent.action.MEDIA_UNMOUNTED");
        hashSet2.add("android.intent.action.MEDIA_CHECKING");
        hashSet2.add("android.intent.action.MEDIA_NOFS");
        hashSet2.add("android.intent.action.MEDIA_MOUNTED");
        hashSet2.add("android.intent.action.MEDIA_SHARED");
        hashSet2.add("android.intent.action.MEDIA_BAD_REMOVAL");
        hashSet2.add("android.intent.action.MEDIA_UNMOUNTABLE");
        hashSet2.add("android.intent.action.MEDIA_EJECT");
        hashSet2.add("android.intent.action.AIRPLANE_MODE");
    }

    public static native e a();

    public static native IntentFilter b(IntentFilter intentFilter, int i4, String str, int i5);

    public static native void c(Intent intent);

    public static native String d(int i4, String str, String str2, int i5);
}
