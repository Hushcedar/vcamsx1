package q;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.os.perationCompat;
import t1.h;

/* JADX INFO: loaded from: classes.dex */
public final class t extends h.a implements Runnable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final PendingIntent.OnFinished f1277b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final PendingIntent f1278c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Handler f1279d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public Intent f1280e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public int f1281f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public String f1282g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public Bundle f1283h;

    static {
        perationCompat.init0(479);
    }

    public t(PendingIntent.OnFinished onFinished, PendingIntent pendingIntent, Handler handler) {
        this.f1277b = onFinished;
        this.f1278c = pendingIntent;
        this.f1279d = handler;
    }

    @Override // t1.h
    public final native void a1(int i4, Intent intent, Bundle bundle, String str);

    @Override // java.lang.Runnable
    public final native void run();
}
