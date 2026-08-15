package k3;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f923a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final p f924b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final r3.h f925c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Object f926d = new Object();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final a f927e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ArrayList f928f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final ArrayList<v> f929g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final SparseArray<v> f930h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f931i;

    public final class a extends u<v> {
        static {
            perationCompat.init0(698);
        }

        public final native void b(int i4, String str);
    }

    static {
        perationCompat.init0(297);
    }

    public t(Context context, p pVar) {
        new HashMap();
        new HashMap();
        new HashMap();
        new HashMap();
        this.f927e = new a();
        this.f928f = new ArrayList(1);
        this.f929g = new ArrayList<>();
        this.f930h = new SparseArray<>();
        this.f931i = 0;
        this.f923a = context;
        this.f924b = pVar;
        context.getPackageName();
        this.f925c = r3.h.M3();
    }

    public final native int a();

    public final native void b(v vVar);

    public final native v c(int i4);

    public final native ArrayList d(int i4, String str, int i5);

    public final native v e(int i4, int i5, ApplicationInfo applicationInfo, String str);

    public final native void f(int i4);

    public final native v g(int i4, int i5, ApplicationInfo applicationInfo, String str);

    public final native v h(ActivityInfo activityInfo, int i4, int i5, String str);

    public final native boolean i(v vVar);
}
