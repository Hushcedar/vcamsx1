package x1;

import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public final class x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "realActivity")
    private static v1.c<ComponentName> f1859a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "topActivityInfo")
    private static v1.c<ActivityInfo> f1860b;

    static {
        if (Build.VERSION.SDK_INT >= 29) {
            j.e.r(x.class, TaskInfo.class);
        }
    }

    public static void a(Object obj, ComponentName componentName) {
        v1.c<ComponentName> cVar = f1859a;
        if (cVar != null) {
            cVar.b(obj, componentName);
        }
    }

    public static void b(Object obj, ActivityInfo activityInfo) {
        v1.c<ActivityInfo> cVar = f1860b;
        if (cVar != null) {
            cVar.b(obj, activityInfo);
        }
    }
}
