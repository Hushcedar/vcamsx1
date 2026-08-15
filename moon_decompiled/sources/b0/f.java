package b0;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final RemoteViews f41a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HashMap f42b;

    public class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Rect f43a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final PendingIntent f44b;

        public a(Rect rect, PendingIntent pendingIntent) {
            this.f43a = rect;
            this.f44b = pendingIntent;
        }
    }

    static {
        perationCompat.init0(526);
    }

    public f(RemoteViews remoteViews) {
        this.f41a = remoteViews;
    }

    public static native Rect b(View view);

    public static native void c(RemoteViews remoteViews, ViewGroup viewGroup, ArrayList arrayList);

    public final native int a();

    public final native void d(RemoteViews remoteViews, View view, View view2);
}
