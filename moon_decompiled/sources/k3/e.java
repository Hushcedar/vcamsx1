package k3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final p f769a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public ActivityInfo f770b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ActivityInfo f771c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ActivityInfo f772d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public ActivityInfo f773e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public ActivityInfo f774f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public ActivityInfo f775g;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public d f779k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final d f780l;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final ArrayList<b> f776h = new ArrayList<>();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final ArrayList<b> f777i = new ArrayList<>();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final ArrayList<b> f778j = new ArrayList<>();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final a f781m = new a();

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public b f782a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public boolean f783b;
    }

    static {
        perationCompat.init0(193);
    }

    public e(p pVar) {
        ActivityInfo[] activityInfoArr;
        String str;
        this.f769a = pVar;
        this.f780l = new d(this, pVar);
        Context context = pVar.f857b;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo == null || (activityInfoArr = packageInfo.activities) == null) {
                return;
            }
            for (ActivityInfo activityInfo : activityInfoArr) {
                if ((activityInfo.flags & 1) != 0 && (str = activityInfo.name) != null && str.startsWith("com.hack.agent.HackAppActivity")) {
                    boolean zA = v3.a.a(activityInfo);
                    int i4 = activityInfo.screenOrientation;
                    if (i4 == 0) {
                        if (zA) {
                            this.f773e = activityInfo;
                        } else {
                            this.f772d = activityInfo;
                        }
                    } else if (i4 == 1) {
                        if (zA) {
                            this.f771c = activityInfo;
                        } else {
                            this.f770b = activityInfo;
                        }
                    } else if (i4 == 3) {
                        if (zA) {
                            this.f775g = activityInfo;
                        } else {
                            this.f774f = activityInfo;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e4) {
            e4.printStackTrace();
        }
    }

    public final native void a(v vVar);

    public final native b b(int i4, Intent intent, ActivityInfo activityInfo, boolean z3);

    public final native int c(b bVar, v vVar, Intent intent, Bundle bundle, boolean z3);

    public final native void d(boolean z3, b bVar);
}
