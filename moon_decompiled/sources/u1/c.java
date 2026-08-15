package u1;

import android.content.pm.ApplicationInfo;
import android.os.ConditionVariable;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ConcurrentHashMap<String, com.core.hack.handle.c> f1643a = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public s1.a f1644b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final CountDownLatch f1645c = new CountDownLatch(4);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ConditionVariable f1646d = new ConditionVariable();

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final c f1647a = new c();
    }

    static {
        perationCompat.init0(221);
    }

    public c() {
        new HashMap();
        new AtomicBoolean(false);
    }

    public static native c a();

    public native void b(String str, com.core.hack.handle.c cVar);

    public native void c(ApplicationInfo applicationInfo);

    public native void d(String str);

    public native s1.a e();

    public native com.core.hack.handle.c f(String str);

    public native void g();

    public native void h();

    public native void i();

    public final native void j();

    public native void k(long j4);
}
