package x1;

import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1827a = j.e.q(j.class, "android.app.IActivityManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final IInterface f1828b = a.a();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "getTaskForActivity", value = {IBinder.class, boolean.class})
    private static v1.f<Integer> f1829c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.e(name = "finishActivity", value = {IBinder.class, int.class, Intent.class, int.class})
    private static v1.f<Boolean> f1830d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.e(name = "setRequestedOrientation", value = {IBinder.class, int.class})
    private static v1.f<Void> f1831e;

    public static void a(IBinder iBinder) {
        v1.f<Boolean> fVar = f1830d;
        if (fVar != null) {
            fVar.a(f1828b, new Object[]{iBinder, 0, null, 0});
        }
    }

    public static int b(IBinder iBinder) {
        v1.f<Integer> fVar = f1829c;
        if (fVar != null) {
            return fVar.a(a.a(), new Object[]{iBinder, Boolean.FALSE}).intValue();
        }
        return -1;
    }

    public static void c(int i4, IBinder iBinder) {
        v1.f<Void> fVar = f1831e;
        if (fVar != null) {
            fVar.a(f1828b, new Object[]{iBinder, Integer.valueOf(i4)});
        }
    }
}
