package k3;

import android.util.ArraySet;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final x f825a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final q f826b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final v f827c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ArraySet<l> f828d = new ArraySet<>();

    static {
        perationCompat.init0(274);
    }

    public h(x xVar, q qVar, v vVar) {
        this.f825a = xVar;
        this.f826b = qVar;
        this.f827c = vVar;
    }

    public final native String toString();
}
