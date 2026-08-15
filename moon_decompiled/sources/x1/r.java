package x1;

import android.app.PendingIntent;
import android.os.Handler;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class r {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mTarget")
    private static v1.c<IInterface> f1846a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "PENDING_INTENT_EXPLICIT_MUTABILITY_REQUIRED")
    public static v1.g<Long> f1847b;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "mWho")
        private static v1.c<PendingIntent.OnFinished> f1848a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "mPendingIntent")
        private static v1.c<PendingIntent> f1849b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "mHandler")
        private static v1.c<Handler> f1850c;

        static {
            j.e.q(a.class, "android.app.PendingIntent$FinishedDispatcher");
        }

        public static Handler a(Object obj) {
            v1.c<Handler> cVar = f1850c;
            if (cVar != null) {
                return cVar.a(obj);
            }
            return null;
        }

        public static PendingIntent b(Object obj) {
            v1.c<PendingIntent> cVar = f1849b;
            if (cVar != null) {
                return cVar.a(obj);
            }
            return null;
        }

        public static PendingIntent.OnFinished c(Object obj) {
            v1.c<PendingIntent.OnFinished> cVar = f1848a;
            if (cVar != null) {
                return cVar.a(obj);
            }
            return null;
        }
    }

    static {
        j.e.r(r.class, PendingIntent.class);
    }

    public static IInterface a(PendingIntent pendingIntent) {
        v1.c<IInterface> cVar = f1846a;
        if (cVar != null) {
            return cVar.a(pendingIntent);
        }
        return null;
    }
}
