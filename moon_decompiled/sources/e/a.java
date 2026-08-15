package e;

import androidx.core.os.perationCompat;
import java.util.concurrent.CountDownLatch;
import t1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a extends g.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ CountDownLatch f280a;

    static {
        perationCompat.init0(209);
    }

    public a(CountDownLatch countDownLatch) {
        this.f280a = countDownLatch;
    }

    @Override // t1.g
    public final native void c1(int i4);
}
