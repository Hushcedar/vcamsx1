package k3;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public final class z {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final /* synthetic */ int f1001g = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f1002a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final p f1003b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final r3.h f1004c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final a f1005d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final SparseArray<Map<String, Map<Pair<String, String>, BroadcastReceiver>>> f1006e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ReentrantLock f1007f;

    public class a extends Handler {
        static {
            perationCompat.init0(33);
        }

        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    public class b extends BroadcastReceiver {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public v f1008a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ActivityInfo f1009b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final ComponentName f1010c;

        static {
            perationCompat.init0(35);
        }

        public b(v vVar, ActivityInfo activityInfo) {
            this.f1008a = vVar;
            this.f1009b = activityInfo;
            this.f1010c = new ComponentName(activityInfo.packageName, activityInfo.name);
        }

        @Override // android.content.BroadcastReceiver
        public final native void onReceive(Context context, Intent intent);
    }

    static {
        perationCompat.init0(257);
    }

    public z(Context context) {
        HandlerThread handlerThread = new HandlerThread("STATIC_RECEIVER_THREAD");
        this.f1006e = new SparseArray<>();
        this.f1007f = new ReentrantLock();
        this.f1002a = context;
        this.f1003b = p.S3();
        this.f1004c = r3.h.M3();
        handlerThread.start();
        this.f1005d = new a(handlerThread.getLooper());
    }

    public final native Map<Pair<String, String>, BroadcastReceiver> a(int i4, String str, boolean z3);
}
