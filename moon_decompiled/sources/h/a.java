package h;

import androidx.core.os.perationCompat;
import g.b;
import g.c;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: h.a$a, reason: collision with other inner class name */
    public class C0023a extends b {
        static {
            perationCompat.init0(655);
        }

        @Override // g.b
        public final native boolean a(Object[] objArr);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public a() {
        Object objA;
        if (g3.a.d()) {
            try {
                objA = g3.a.a();
                if (objA == null) {
                    g3.a.c(g3.a.f385c.newInstance());
                    objA = g3.a.a();
                }
            } catch (Throwable th) {
                th.printStackTrace();
                objA = null;
            }
        } else {
            objA = null;
        }
        super(objA);
    }
}
