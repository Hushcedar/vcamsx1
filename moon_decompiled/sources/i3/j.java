package i3;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Object f474a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final HashMap f475b;

    static {
        perationCompat.init0(134);
        f474a = new Object();
        f475b = new HashMap();
    }

    public static native <T extends k> T a(Class<T> cls);

    public static native <T extends k> void b(T t4);
}
