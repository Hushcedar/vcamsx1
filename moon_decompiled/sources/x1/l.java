package x1;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "connected", value = {ComponentName.class, IBinder.class})
    private static v1.f<Void> f1834a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "connected", value = {ComponentName.class, IBinder.class, boolean.class})
    private static v1.f<Void> f1835b;

    static {
        j.e.r(l.class, IServiceConnection.class);
    }

    public static void a(IServiceConnection iServiceConnection, ComponentName componentName, IBinder iBinder, boolean z3) {
        v1.f<Void> fVar = f1835b;
        if (fVar != null) {
            fVar.a(iServiceConnection, new Object[]{componentName, iBinder, Boolean.valueOf(z3)});
            return;
        }
        v1.f<Void> fVar2 = f1834a;
        if (fVar2 != null) {
            fVar2.a(iServiceConnection, new Object[]{componentName, iBinder});
        }
    }
}
