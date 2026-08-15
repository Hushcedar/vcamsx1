package x1;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.ArraySet;

/* JADX INFO: loaded from: classes.dex */
public final class q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @TargetApi(26)
    @v1.b(name = "mChannelId")
    private static v1.c<String> f1841a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mGroupKey")
    private static v1.c<String> f1842b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "allPendingIntents")
    private static v1.c<ArraySet<PendingIntent>> f1843c;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "rebuild", value = {Context.class, Notification.class})
        private static v1.h<Notification> f1844a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.e(name = "recoverBuilder", value = {Context.class, Notification.class})
        private static v1.h<Notification.Builder> f1845b;

        static {
            j.e.r(a.class, Notification.Builder.class);
        }

        public static Notification.Builder a(Context context, Notification notification) {
            v1.h<Notification.Builder> hVar = f1845b;
            if (hVar != null) {
                return hVar.a(new Object[]{context, notification});
            }
            v1.h<Notification> hVar2 = f1844a;
            if (hVar2 == null) {
                return null;
            }
            hVar2.a(new Object[]{context, notification});
            return null;
        }
    }

    static {
        j.e.r(q.class, Notification.class);
    }

    public static ArraySet<PendingIntent> a(Notification notification) {
        v1.c<ArraySet<PendingIntent>> cVar = f1843c;
        if (cVar != null) {
            return cVar.a(notification);
        }
        return null;
    }

    public static void b(Notification notification, ArraySet arraySet) {
        v1.c<ArraySet<PendingIntent>> cVar = f1843c;
        if (cVar != null) {
            cVar.b(notification, arraySet);
        }
    }

    public static void c(Notification notification, String str) {
        v1.c<String> cVar = f1841a;
        if (cVar != null) {
            cVar.b(notification, str);
        }
    }

    public static void d(Notification notification, String str) {
        v1.c<String> cVar = f1842b;
        if (cVar != null) {
            cVar.b(notification, str);
        }
    }
}
