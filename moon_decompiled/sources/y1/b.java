package y1;

import android.app.job.JobParameters;
import android.os.IBinder;
import j.e;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "jobId")
    private static c<Integer> f1879a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "callback")
    private static c<IBinder> f1880b;

    static {
        e.q(b.class, JobParameters.class.getName());
    }

    public static IBinder a(JobParameters jobParameters) {
        c<IBinder> cVar = f1880b;
        if (cVar != null) {
            return cVar.a(jobParameters);
        }
        return null;
    }

    public static void b(JobParameters jobParameters, IBinder iBinder) {
        c<IBinder> cVar = f1880b;
        if (cVar != null) {
            cVar.b(jobParameters, iBinder);
        }
    }

    public static void c(JobParameters jobParameters, int i4) {
        c<Integer> cVar = f1879a;
        if (cVar != null) {
            cVar.b(jobParameters, Integer.valueOf(i4));
        }
    }
}
