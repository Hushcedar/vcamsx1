package k3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class a0 {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static a f721l;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f722a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f723b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ComponentName f724c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ComponentName f725d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public Intent f726e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final int f727f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public String f728g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final ArrayList<b> f729h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final d f730i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public int f731j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final String f732k;

    public static class a {
    }

    static {
        perationCompat.init0(712);
        f721l = new a();
    }

    public a0(d dVar, int i4, int i5, ActivityInfo activityInfo, Intent intent, String str) {
        this.f730i = dVar;
        this.f722a = i4;
        this.f727f = i5;
        int i6 = activityInfo.applicationInfo.uid;
        this.f732k = str;
        this.f729h = new ArrayList<>();
        e(intent, activityInfo, i4);
    }

    public final native void a(b bVar);

    public final native b b(boolean z3);

    public final native b c();

    public final native b d(b bVar, int i4);

    public final native void e(Intent intent, ActivityInfo activityInfo, int i4);

    public final native void f(b bVar);

    public final native b g();
}
