package n;

import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final boolean f1122e = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final String f1123f = "c";

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final boolean f1124g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final String f1125h = "android.uwb.IUwbAdapter";

    static {
        perationCompat.init0(587);
    }

    public c() {
        super(f1125h);
    }

    @Override // com.core.hack.handle.c
    public native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public native HashMap<String, com.core.hack.handle.b> c();
}
