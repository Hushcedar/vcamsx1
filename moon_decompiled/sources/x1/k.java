package x1;

import android.app.ActivityManager;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getTaskInfo", value = {})
    private static v1.f<ActivityManager.RecentTaskInfo> f1832a;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "asInterface", value = {IBinder.class})
        private static v1.h<Object> f1833a;

        static {
            j.e.q(a.class, "android.app.IAppTask$Stub");
        }

        public static Object a(IBinder iBinder) {
            v1.h<Object> hVar = f1833a;
            if (hVar != null) {
                return hVar.a(new Object[]{iBinder});
            }
            return null;
        }
    }

    static {
        j.e.q(k.class, "android.app.IAppTask");
    }

    public static ActivityManager.RecentTaskInfo a(Object obj) {
        v1.f<ActivityManager.RecentTaskInfo> fVar = f1832a;
        if (fVar != null) {
            return fVar.a(obj, j.e.f514r);
        }
        return null;
    }
}
