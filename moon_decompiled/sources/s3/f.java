package s3;

import androidx.core.os.perationCompat;
import s3.e;

/* JADX INFO: loaded from: classes.dex */
public final class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ i f1506a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean f1507b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ e.b f1508c;

    static {
        perationCompat.init0(5);
    }

    public f(e.b bVar, i iVar, boolean z3) {
        this.f1508c = bVar;
        this.f1506a = iVar;
        this.f1507b = z3;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
