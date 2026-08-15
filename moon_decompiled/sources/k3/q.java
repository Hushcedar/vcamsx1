package k3;

import android.content.Intent;
import android.os.IBinder;
import android.util.ArrayMap;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final x f886a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Intent.FilterComparison f887b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ArrayMap<v, h> f888c = new ArrayMap<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public IBinder f889d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f890e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f891f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f892g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public boolean f893h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public String f894i;

    static {
        perationCompat.init0(294);
    }

    public q(x xVar, Intent.FilterComparison filterComparison) {
        this.f886a = xVar;
        this.f887b = filterComparison;
    }

    public final native String toString();
}
