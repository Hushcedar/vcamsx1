package r3;

import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1382c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final SparseArray<y> f1383a = new SparseArray<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final b f1384b = new b();

    public class a extends v3.k<i> {
        static {
            perationCompat.init0(137);
        }

        @Override // v3.k
        public final native i a();
    }

    public class b extends v3.g {
        static {
            perationCompat.init0(140);
        }

        public b() {
            super(new File(p.a.l(), "userIds.list"));
        }

        @Override // v3.g, v3.f
        public final native void a(JSONObject jSONObject);

        @Override // v3.g
        public final native void i(JSONObject jSONObject);
    }

    static {
        perationCompat.init0(748);
        f1382c = new a();
    }

    public static native i b();

    public static native int e(int i4, int i5);

    public final native boolean a(int i4);

    public final native int[] c(boolean z3);

    public final native ArrayList d(boolean z3);

    public final native boolean f(int i4);

    public final native void g(int i4);
}
