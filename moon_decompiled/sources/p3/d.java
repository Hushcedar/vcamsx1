package p3;

import android.app.job.IJobCallback;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import i.i;
import java.lang.ref.WeakReference;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f1213d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final c f1214a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseArray<b> f1216c = new SparseArray<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final IJobServiceStubC0071d f1215b = new IJobServiceStubC0071d(this);

    public class a extends k<d> {
        static {
            perationCompat.init0(715);
        }

        @Override // v3.k
        public final native d a();
    }

    public final class b extends IJobCallback.Stub {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f1217a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final IJobCallback f1218b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final JobParameters f1219c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public IJobService f1220d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final int f1221e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final e f1222f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public f.b f1223g;

        static {
            perationCompat.init0(717);
        }

        public b(int i4, IJobCallback iJobCallback, JobParameters jobParameters, int i5) {
            this.f1217a = i4;
            this.f1218b = iJobCallback;
            this.f1219c = jobParameters;
            this.f1221e = i5;
            this.f1222f = d.this.new e(this);
        }

        public final native void acknowledgeStartMessage(int i4, boolean z3);

        public final native void acknowledgeStopMessage(int i4, boolean z3);

        public final native boolean completeWork(int i4, int i5);

        public final native JobWorkItem dequeueWork(int i4);

        public final native void jobFinished(int i4, boolean z3);
    }

    public class c extends Handler {
        static {
            perationCompat.init0(718);
        }

        public c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    /* JADX INFO: renamed from: p3.d$d, reason: collision with other inner class name */
    public static final class IJobServiceStubC0071d extends IJobService.Stub {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final WeakReference<d> f1226a;

        static {
            perationCompat.init0(726);
        }

        public IJobServiceStubC0071d(d dVar) {
            this.f1226a = new WeakReference<>(dVar);
        }

        public final native void getTransferredDownloadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem);

        public final native void getTransferredUploadBytes(JobParameters jobParameters, JobWorkItem jobWorkItem);

        public final native void onNetworkChanged(JobParameters jobParameters);

        public final native void startJob(JobParameters jobParameters);

        public final native void stopJob(JobParameters jobParameters);
    }

    public final class e extends i.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final b f1227a;

        static {
            perationCompat.init0(727);
        }

        public e(b bVar) {
            this.f1227a = bVar;
        }

        @Override // i.i.a, i.i
        public final native void onBindingDied(ComponentName componentName);

        @Override // i.i.a, i.i
        public final native void onNullBinding(ComponentName componentName);

        @Override // i.i.a, i.i
        public final native void onServiceConnected(ComponentName componentName, IBinder iBinder);

        @Override // i.i.a, i.i
        public final native void onServiceDisconnected(ComponentName componentName);
    }

    static {
        perationCompat.init0(629);
        f1213d = new a();
    }

    public d() {
        HandlerThread handlerThread = new HandlerThread("JobServiceEngine");
        handlerThread.start();
        this.f1214a = new c(handlerThread.getLooper());
    }

    public static native void a(d dVar, JobParameters jobParameters);

    public static native void b(d dVar, JobParameters jobParameters);

    public static native void c(d dVar);

    public static native void d(IJobCallback iJobCallback, int i4);

    public static native d e();
}
