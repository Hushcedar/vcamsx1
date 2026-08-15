package i;

import androidx.core.os.perationCompat;
import java.util.concurrent.CountDownLatch;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ d f396a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean[] f397b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String f398c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f399d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final /* synthetic */ CountDownLatch f400e;

    static {
        perationCompat.init0(41);
    }

    public /* synthetic */ c(d dVar, boolean[] zArr, String str, int i4, CountDownLatch countDownLatch) {
        this.f396a = dVar;
        this.f397b = zArr;
        this.f398c = str;
        this.f399d = i4;
        this.f400e = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
