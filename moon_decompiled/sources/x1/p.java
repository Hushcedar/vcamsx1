package x1;

import android.annotation.TargetApi;
import android.app.NotificationChannel;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(26)
public final class p {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mId")
    private static v1.c<String> f1840a;

    static {
        j.e.r(p.class, NotificationChannel.class);
    }

    public static void a(NotificationChannel notificationChannel, String str) {
        v1.c<String> cVar = f1840a;
        if (cVar != null) {
            cVar.b(notificationChannel, str);
        }
    }
}
