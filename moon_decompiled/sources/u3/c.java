package u3;

import android.content.Context;
import android.content.pm.PackageInfo;
import androidx.core.os.perationCompat;
import r3.h;

/* JADX INFO: loaded from: classes.dex */
public final class c extends Thread {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f1683a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public PackageInfo f1684b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public PackageInfo f1685c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final a f1686d;

    static {
        perationCompat.init0(149);
    }

    public c(Context context, h.c cVar) {
        this.f1683a = context;
        this.f1686d = cVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final native void run();
}
