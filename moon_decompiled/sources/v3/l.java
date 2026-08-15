package v3;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Handler f1737a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static volatile ThreadPoolExecutor f1738b;

    static {
        Looper.getMainLooper().getThread();
        f1737a = new Handler(Looper.getMainLooper());
    }

    public static void a(Runnable runnable) {
        if (f1738b == null) {
            synchronized (l.class) {
                if (f1738b == null) {
                    int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
                    f1738b = new ThreadPoolExecutor(2, iAvailableProcessors > 12 ? 12 : iAvailableProcessors, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue());
                    f1738b.allowCoreThreadTimeOut(true);
                }
            }
        }
        f1738b.submit(runnable);
    }
}
