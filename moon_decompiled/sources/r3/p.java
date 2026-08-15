package r3;

import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class p extends t {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final p f1411b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final t[] f1412a;

    static {
        perationCompat.init0(724);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new b());
        arrayList.add(new o());
        f1411b = new p((t[]) arrayList.toArray(new t[0]));
    }

    public p(t[] tVarArr) {
        this.f1412a = tVarArr;
    }

    @Override // r3.t
    public final native void a(t3.a aVar);
}
