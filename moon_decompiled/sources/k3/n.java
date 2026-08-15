package k3;

import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class n implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f846a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ p f847b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f848c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Object f849d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ Object f850e;

    static {
        perationCompat.init0(284);
    }

    public /* synthetic */ n(p pVar, Object obj, int i4, Object obj2, int i5) {
        this.f846a = i5;
        this.f847b = pVar;
        this.f849d = obj;
        this.f848c = i4;
        this.f850e = obj2;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
