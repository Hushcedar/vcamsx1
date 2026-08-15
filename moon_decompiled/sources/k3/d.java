package k3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final SparseArray<a0> f765a = new SparseArray<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public b f766b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ArrayList<b> f767c = new ArrayList<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final e f768d;

    static {
        perationCompat.init0(188);
    }

    public d(e eVar, p pVar) {
        this.f768d = eVar;
    }

    public static native boolean d(b bVar, int i4, Intent intent, boolean z3);

    public static native void f(a0 a0Var, b bVar);

    public static native void g(b bVar, String str, int i4, int i5, Intent intent);

    public final native void a(b bVar);

    public final native void b(b bVar);

    public final native a0 c(int i4, int i5, ActivityInfo activityInfo, Intent intent, String str);

    public final native b e(b bVar);

    public final native a0 h(int i4);
}
