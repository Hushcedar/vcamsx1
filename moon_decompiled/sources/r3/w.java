package r3;

import android.util.ArrayMap;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashMap;
import t3.a;

/* JADX INFO: loaded from: classes.dex */
public final class w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f1449a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ArrayMap<String, a.f> f1450b = new ArrayMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ArrayMap<String, a.g> f1451c = new ArrayMap<>();

    static {
        perationCompat.init0(728);
    }

    public w(HashMap map) {
        this.f1449a = map;
    }

    public final native void a(a.e eVar);

    public final native ArrayList b(int i4);

    public final native a.f c(String str);

    public final native a.g d(String str);

    public final native void e(a.e eVar);

    public final native ArrayList f(int i4, String str);
}
