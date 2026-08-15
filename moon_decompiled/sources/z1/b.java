package z1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import j.e;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1902a = e.q(b.class, "android.app.servertransaction.LaunchActivityItem");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mIntent")
    private static v1.c<Intent> f1903b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "mReferrer")
    private static v1.c<String> f1904c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "mInfo")
    private static v1.c<ActivityInfo> f1905d;

    public static void a(Object obj) {
        v1.c<ActivityInfo> cVar = f1905d;
        if (cVar != null) {
            cVar.a(obj);
        }
    }

    public static Intent b(Object obj) {
        v1.c<Intent> cVar = f1903b;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }

    public static void c(Object obj, ActivityInfo activityInfo) {
        v1.c<ActivityInfo> cVar = f1905d;
        if (cVar != null) {
            cVar.b(obj, activityInfo);
        }
    }

    public static void d(Object obj, Intent intent) {
        v1.c<Intent> cVar = f1903b;
        if (cVar != null) {
            cVar.b(obj, intent);
        }
    }

    public static void e(Object obj, String str) {
        v1.c<String> cVar = f1904c;
        if (cVar != null) {
            cVar.b(obj, str);
        }
    }
}
