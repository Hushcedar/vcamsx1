package j;

import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ j f526a;

    static {
        perationCompat.init0(10);
    }

    public i(j jVar) {
        this.f526a = jVar;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
