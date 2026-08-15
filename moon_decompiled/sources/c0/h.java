package c0;

import android.os.IBinder;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class h extends com.core.hack.handle.a {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final com.core.hack.handle.b f89h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public volatile IBinder f90i;

    static {
        perationCompat.init0(627);
    }

    public h(m mVar) {
        this.f89h = mVar;
    }

    @Override // com.core.hack.handle.b
    public final native Object[] a();

    @Override // com.core.hack.handle.b
    public final native Class[] e();

    @Override // com.core.hack.handle.a
    public final native Object m(Object[] objArr);
}
