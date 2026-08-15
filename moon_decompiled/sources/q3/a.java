package q3;

import android.app.NotificationManager;
import android.content.Context;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.p;
import java.util.HashMap;
import java.util.List;
import t1.b;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a extends p.a {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final C0075a f1295e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public NotificationManager f1297b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f1296a = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseArray<HashMap<String, List<t1.a>>> f1298c = new SparseArray<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final SparseArray<HashMap<String, List<b>>> f1299d = new SparseArray<>();

    /* JADX INFO: renamed from: q3.a$a, reason: collision with other inner class name */
    public class C0075a extends k<a> {
        static {
            perationCompat.init0(102);
        }

        @Override // v3.k
        public final native a a();
    }

    static {
        perationCompat.init0(639);
        f1295e = new C0075a();
    }

    public static native a H3();

    @Override // i.p.a, i.p
    public final native void F0(String str, List<b> list, int i4);

    public final native List I3(int i4, String str);

    public final native void J3(Context context);

    public final native void K3();

    @Override // i.p.a, i.p
    public final native List<b> L2(String str, int i4);

    @Override // i.p.a, i.p
    public final native void P2(String str, String str2, int i4, int i5);

    @Override // i.p.a, i.p
    public final native void Z(String str, String str2, int i4, int i5);

    @Override // i.p.a, i.p
    public final native void n0(String str, List<b> list, int i4);

    @Override // i.p.a, i.p
    public final native void u3(String str, int i4);

    @Override // i.p.a, i.p
    public final native int w1(String str, int i4);
}
