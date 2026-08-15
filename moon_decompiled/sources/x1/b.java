package x1;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.os.IInterface;
import x1.k;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getTaskService", value = {})
    private static v1.h<IInterface> f1774a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "getService", value = {})
    private static v1.h<IInterface> f1775b;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.d(name = "", value = {@v1.i(strings = {"android.app.IAppTask"}, type = 1)})
        private static v1.a<ActivityManager.AppTask> f1776a;

        static {
            j.e.r(a.class, ActivityManager.AppTask.class);
        }

        public static ActivityManager.AppTask a(IBinder iBinder) {
            v1.a<ActivityManager.AppTask> aVar = f1776a;
            if (aVar != null) {
                return aVar.a(new Object[]{k.a.a(iBinder)});
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: x1.b$b, reason: collision with other inner class name */
    public static class C0097b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "", value = {String.class, int.class, boolean.class, int.class})
        public static v1.a<Object> f1777a;

        static {
            j.e.q(C0097b.class, "android.app.ActivityManager$PendingIntentInfo");
        }
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "realActivity")
        private static v1.c<ComponentName> f1778a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "topActivityInfo")
        private static v1.c<ActivityInfo> f1779b;

        static {
            j.e.r(c.class, ActivityManager.RecentTaskInfo.class);
        }

        public static void a(Object obj, ComponentName componentName) {
            v1.c<ComponentName> cVar = f1778a;
            if (cVar != null) {
                cVar.b(obj, componentName);
            } else {
                x.a(obj, componentName);
            }
        }

        public static void b(Object obj, ActivityInfo activityInfo) {
            v1.c<ActivityInfo> cVar = f1779b;
            if (cVar != null) {
                cVar.b(obj, activityInfo);
            } else {
                x.b(obj, activityInfo);
            }
        }
    }

    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "realActivity")
        private static v1.c<ComponentName> f1780a;

        static {
            j.e.r(d.class, ActivityManager.RunningTaskInfo.class);
        }

        public static void a(Object obj, ComponentName componentName) {
            v1.c<ComponentName> cVar = f1780a;
            if (cVar != null) {
                cVar.b(obj, componentName);
            } else {
                x.a(obj, componentName);
            }
        }
    }

    static {
        j.e.r(b.class, ActivityManager.class);
    }

    public static IInterface a() {
        v1.h<IInterface> hVar = f1775b;
        if (hVar != null) {
            return hVar.a(j.e.f514r);
        }
        return null;
    }

    public static IInterface b() {
        v1.h<IInterface> hVar = f1774a;
        if (hVar != null) {
            return hVar.a(j.e.f514r);
        }
        return null;
    }
}
