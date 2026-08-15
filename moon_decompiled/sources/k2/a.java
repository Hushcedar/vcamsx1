package k2;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "setDefaultExecutor", value = {Executor.class})
    private static v1.h<Void> f683a;

    static {
        j.e.r(a.class, AsyncTask.class);
    }

    public static void a(Executor executor) {
        v1.h<Void> hVar = f683a;
        if (hVar != null) {
            hVar.a(new Object[]{executor});
        }
    }
}
