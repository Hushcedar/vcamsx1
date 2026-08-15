package w2;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import j.e;
import java.util.ArrayList;
import v1.f;
import v1.i;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mActions")
    private static v1.c<ArrayList<Notification.Action>> f1743a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mApplication")
    private static v1.c<ApplicationInfo> f1744b;

    /* JADX INFO: renamed from: w2.a$a, reason: collision with other inner class name */
    public static class C0095a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.b(name = "viewId")
        private static v1.c<Integer> f1745a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.d(name = "apply", value = {@i(classes = {View.class, ViewGroup.class}, type = 0), @i(strings = {"android.widget.RemoteViews$OnClickHandler"}, type = 1)})
        private static f<String> f1746b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.d(name = "apply", value = {@i(classes = {View.class, ViewGroup.class}, type = 0), @i(strings = {"android.widget.RemoteViews$InteractionHandler", "android.widget.RemoteViews$ColorResources"}, type = 1)})
        private static f<Void> f1747c;

        static {
            e.q(C0095a.class, "android.widget.RemoteViews$Action");
        }

        public static void b(Notification.Action action, View view) {
            f<String> fVar = f1746b;
            if (fVar != null) {
                fVar.a(action, new Object[]{view, null, null});
                return;
            }
            f<Void> fVar2 = f1747c;
            if (fVar2 != null) {
                fVar2.a(action, new Object[]{view, null, null, null});
            }
        }
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class<?> f1748a = e.q(b.class, "android.widget.RemoteViews$ReflectionAction");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "viewId")
        private static v1.c<Integer> f1749b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "methodName")
        private static v1.c<String> f1750c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @v1.b(name = "type")
        private static v1.c<Integer> f1751d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @v1.b(name = "value")
        private static v1.c<Object> f1752e;

        public static String a(Notification.Action action) {
            v1.c<String> cVar = f1750c;
            if (cVar != null) {
                return cVar.a(action);
            }
            return null;
        }

        public static int b(Notification.Action action) {
            v1.c<Integer> cVar = f1751d;
            if (cVar != null) {
                return cVar.a(action).intValue();
            }
            return -1;
        }

        public static void c(Notification.Action action) {
            v1.c<Integer> cVar = f1751d;
            if (cVar != null) {
                cVar.b(action, 9);
            }
        }

        public static Object d(Notification.Action action) {
            v1.c<Object> cVar = f1752e;
            if (cVar != null) {
                return cVar.a(action);
            }
            return null;
        }

        public static void e(Notification.Action action, String str) {
            v1.c<Object> cVar = f1752e;
            if (cVar != null) {
                cVar.b(action, str);
            }
        }

        public static int f(Notification.Action action) {
            v1.c<Integer> cVar;
            if (C0095a.f1745a != null) {
                cVar = C0095a.f1745a;
            } else {
                cVar = f1749b;
                if (cVar == null) {
                    return -1;
                }
            }
            return cVar.a(action).intValue();
        }
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class<?> f1753a = e.q(c.class, "android.widget.RemoteViews$SetOnClickPendingIntent");

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "viewId")
        private static v1.c<Integer> f1754b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "pendingIntent")
        private static v1.c<PendingIntent> f1755c;

        public static PendingIntent a(Object obj) {
            v1.c<PendingIntent> cVar = f1755c;
            if (cVar != null) {
                return cVar.a(obj);
            }
            return null;
        }

        public static int b(Object obj) {
            v1.c<Integer> cVar;
            if (C0095a.f1745a != null) {
                cVar = C0095a.f1745a;
            } else {
                cVar = f1754b;
                if (cVar == null) {
                    return -1;
                }
            }
            return cVar.a(obj).intValue();
        }
    }

    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final Class<?> f1756a = e.q(d.class, "android.widget.RemoteViews$TextViewDrawableAction");
    }

    static {
        e.r(a.class, RemoteViews.class);
    }

    public static ArrayList<Notification.Action> a(RemoteViews remoteViews) {
        v1.c<ArrayList<Notification.Action>> cVar = f1743a;
        if (cVar != null) {
            return cVar.a(remoteViews);
        }
        return null;
    }

    public static void b(RemoteViews remoteViews, ApplicationInfo applicationInfo) {
        v1.c<ApplicationInfo> cVar = f1744b;
        if (cVar != null) {
            cVar.b(remoteViews, applicationInfo);
        }
    }
}
