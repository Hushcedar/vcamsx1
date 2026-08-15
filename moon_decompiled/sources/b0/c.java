package b0;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
import android.widget.RemoteViews;
import androidx.core.os.perationCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f23c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ArrayList f24a = new ArrayList(10);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final g f25b;

    public class a extends k<c> {
        static {
            perationCompat.init0(623);
        }

        @Override // v3.k
        public final native c a();
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f26a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f27b = "setImageBitmap";

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final Bitmap f28c;

        public b(int i4, Bitmap bitmap) {
            this.f26a = i4;
            this.f28c = bitmap;
        }
    }

    static {
        perationCompat.init0(541);
        f23c = new a();
    }

    public c() {
        for (Field field : y2.b.f1883a.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                try {
                    this.f24a.add(Integer.valueOf(field.getInt(null)));
                } catch (Throwable unused) {
                }
            }
        }
        this.f25b = new g();
    }

    public static native c a();

    public static native void b(Parcelable[] parcelableArr);

    public static native String c(int i4, String str, String str2);

    public static native String d(int i4, String str, String str2);

    public static native String e(String str, String str2, int i4);

    @TargetApi(23)
    public static native void f(Icon icon, Context context, boolean z3);

    public static native boolean j(Context context, RemoteViews remoteViews);

    public static native String k(int i4, String str, String str2);

    public static native void m(Notification notification, ApplicationInfo applicationInfo);

    public final native void g(Resources resources, RemoteViews remoteViews, boolean z3, Notification notification);

    public final native Notification h(int i4, Notification notification, String str, int i5);

    public final native void i(int i4, Notification notification, Context context);

    public final native boolean l(RemoteViews remoteViews);
}
