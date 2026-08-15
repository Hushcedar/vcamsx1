package x1;

import android.app.NotificationChannelGroup;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mId")
    private static v1.c<String> f1839a;

    static {
        if (Build.VERSION.SDK_INT >= 26) {
            j.e.r(o.class, NotificationChannelGroup.class);
        }
    }

    public static void a(NotificationChannelGroup notificationChannelGroup, String str) {
        v1.c<String> cVar = f1839a;
        if (cVar != null) {
            cVar.b(notificationChannelGroup, str);
        }
    }
}
