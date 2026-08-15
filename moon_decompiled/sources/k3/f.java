package k3;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import k3.g;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final g.a f784a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public g f785b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final p f786c;

    static {
        perationCompat.init0(190);
    }

    public f(p pVar) {
        g.a aVar = new g.a(pVar.f860e, pVar);
        this.f784a = aVar;
        aVar.f804a = this;
        this.f786c = pVar;
    }

    public final native int a(f.b bVar, int i4, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i5, String str2);

    public final native void b(int i4, int i5, String str, Intent intent, String str2, IBinder iBinder, String str3, int i6, Bundle bundle, int i7);
}
