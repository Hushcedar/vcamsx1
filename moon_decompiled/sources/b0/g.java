package b0;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final /* synthetic */ int f45h = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f47b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f48c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f49d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public int f50e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final HashMap<String, Bitmap> f51f = new HashMap<>();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f52g = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final h f46a = new h();

    static {
        perationCompat.init0(533);
    }

    public static native void b(ViewGroup viewGroup);

    public static native int c(Context context, Context context2, String str);

    public final native FrameLayout a(Context context, RemoteViews remoteViews, View view, boolean z3);

    public final native RemoteViews d(String str, Context context, RemoteViews remoteViews, boolean z3, boolean z4);
}
