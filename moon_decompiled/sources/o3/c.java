package o3;

import android.content.Context;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.l;
import java.io.File;
import java.util.Map;
import java.util.Random;
import org.json.JSONObject;
import v3.g;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class c extends l.a {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f1172g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1173a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final SparseArray<o3.a> f1174b = new SparseArray<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseArray<Map<String, o3.a>> f1175c = new SparseArray<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final b f1176d = new b();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final Object f1177e = new Object();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public String f1178f;

    public class a extends k<c> {
        static {
            perationCompat.init0(214);
        }

        @Override // v3.k
        public final native c a();
    }

    public class b extends g {
        static {
            perationCompat.init0(212);
        }

        public b() {
            super(new File(p.a.l(), "deviceinfo.list"));
        }

        @Override // v3.g, v3.f
        public final native void a(JSONObject jSONObject);

        @Override // v3.g
        public final native void i(JSONObject jSONObject);
    }

    static {
        perationCompat.init0(534);
        f1172g = new a();
    }

    public static native o3.a H3(c cVar, JSONObject jSONObject);

    public static native String J3(Random random);

    public static native o3.a K3(int i4, String str, boolean z3, boolean z4);

    public static native c L3();

    public static native String M3(Random random, int i4, boolean z3);

    public final native void I3(int i4);

    @Override // i.l.a, i.l
    public final native boolean e0(int i4, String str);

    @Override // i.l.a, i.l
    public final native o3.a i(int i4, String str);

    @Override // i.l.a, i.l
    public final native boolean k2(int i4, boolean z3);
}
