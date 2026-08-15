package e;

import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f313a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Object f314b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Object f315c;

    static {
        perationCompat.init0(210);
    }

    public /* synthetic */ c(Object obj, Object obj2, int i4) {
        this.f313a = i4;
        this.f314b = obj;
        this.f315c = obj2;
    }

    private final native void a();

    @Override // java.lang.Runnable
    public final native void run();
}
