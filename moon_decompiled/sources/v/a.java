package v;

import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final HashSet f1687e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final HashSet f1688f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final HashSet f1689g;

    static {
        perationCompat.init0(184);
        f1687e = new HashSet();
        f1688f = new HashSet();
        f1689g = new HashSet();
    }

    public a() {
        super("android.content.IContentProvider");
        if (t1.c.c()) {
            return;
        }
        d();
    }

    public static native int g(IBinder iBinder);

    public static native void h(IBinder iBinder, String str);

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
