package s3;

import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ e f1483a;

    static {
        perationCompat.init0(1);
    }

    public d(e eVar) {
        this.f1483a = eVar;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
