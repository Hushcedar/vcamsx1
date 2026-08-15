package q;

import android.os.IBinder;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.a {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1235h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final com.core.hack.handle.b f1236i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final h f1237j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public volatile IBinder f1238k;

    static {
        perationCompat.init0(448);
    }

    public c(String str, com.core.hack.handle.b bVar, h hVar) {
        this.f1235h = str;
        this.f1236i = bVar;
        this.f1237j = hVar;
    }

    public static native com.core.hack.handle.a n(String str);

    @Override // com.core.hack.handle.b
    public final native Object[] a();

    @Override // com.core.hack.handle.b
    public final native Class[] e();

    @Override // com.core.hack.handle.a
    public final native Object m(Object[] objArr);

    public final native void o();
}
