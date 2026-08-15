package i;

import android.app.job.JobInfo;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f406b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public o f407a;

    public class a extends v3.k<f> {
        static {
            perationCompat.init0(204);
        }

        @Override // v3.k
        public final native f a();
    }

    static {
        perationCompat.init0(47);
        f406b = new a();
    }

    public static native f c();

    public native int a(int i4);

    public native void b();

    public native List d();

    public native JobInfo e(int i4);

    public native void f();

    public native int g(JobInfo jobInfo);
}
