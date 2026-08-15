package k3;

import android.app.IServiceConnection;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final h f838a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final IServiceConnection f839b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f840c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f841d = 0;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public String f842e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f843f;

    static {
        perationCompat.init0(282);
    }

    public l(h hVar, IServiceConnection iServiceConnection, int i4) {
        this.f838a = hVar;
        this.f839b = iServiceConnection;
        this.f840c = i4;
    }

    public final native String toString();
}
