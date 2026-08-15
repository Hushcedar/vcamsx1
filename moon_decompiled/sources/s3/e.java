package s3;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.util.AtomicFile;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.core.os.perationCompat;
import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import r3.k;

/* JADX INFO: loaded from: classes.dex */
public final class e extends k.a implements j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Context f1484b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final r3.h f1485c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final HandlerThread f1486d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final Handler f1487e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final a f1488f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final AtomicFile f1489g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final File f1490h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final b f1491i = new b();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final SecureRandom f1492j = new SecureRandom();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final SparseBooleanArray f1493k = new SparseBooleanArray();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final SparseArray<i> f1494l = new SparseArray<>();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final ArrayList f1495m = new ArrayList();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final SparseIntArray f1496n = new SparseIntArray();

    public static class a extends Handler {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final /* synthetic */ int f1497b = 0;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final RemoteCallbackList<IPackageInstallerCallback> f1498a;

        static {
            perationCompat.init0(71);
        }

        public a(Looper looper) {
            super(looper);
            this.f1498a = new RemoteCallbackList<>();
        }

        public static native void a(IPackageInstallerCallback iPackageInstallerCallback, Message message);

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    public class b {
        static {
            perationCompat.init0(77);
        }

        public b() {
        }

        public final native void a();
    }

    public static class c extends s3.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Context f1500a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final IntentSender f1501b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f1502c;

        public c(Context context, IntentSender intentSender, String str) {
            this.f1500a = context;
            this.f1501b = intentSender;
            this.f1502c = str;
        }
    }

    public static class d extends s3.b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final Context f1503b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final IntentSender f1504c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final int f1505d;

        public d(Context context, IntentSender intentSender, int i4) {
            this.f1503b = context;
            this.f1504c = intentSender;
            this.f1505d = i4;
        }
    }

    static {
        perationCompat.init0(8);
    }

    public e(Context context, r3.h hVar) {
        new SparseBooleanArray();
        this.f1484b = context;
        this.f1485c = hVar;
        HandlerThread handlerThread = new HandlerThread("PackageInstaller");
        this.f1486d = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        this.f1487e = handler;
        this.f1488f = new a(handlerThread.getLooper());
        this.f1489g = new AtomicFile(new File(p.a.l(), "install_sessions.xml"));
        File file = new File(p.a.l(), "install_sessions");
        this.f1490h = file;
        file.mkdirs();
        final int i4 = 6;
        handler.post(new Runnable() { // from class: b.a
            static {
                perationCompat.init0(374);
            }

            @Override // java.lang.Runnable
            public final native void run();
        });
    }

    public static native void E3(Context context, Intent intent, IntentSender intentSender);

    public static native File H3(int i4);

    public final native void F3(i iVar);

    @Override // r3.k
    public final native int G0(PackageInstaller.SessionParams sessionParams, String str, int i4);

    @Override // r3.k
    public final native void G1(int i4);

    public final native int G3();

    @Override // r3.k
    public final native PackageInstaller.SessionInfo I2(int i4);

    public final native int I3(PackageInstaller.SessionParams sessionParams, String str, int i4);

    public final native i J3(int i4);

    public final native i K3(int i4);

    public final native void L3();

    public final native void M3();

    @Override // r3.k
    public final native void N2(int i4, boolean z3);

    public final native void N3();

    @Override // r3.k
    public final native void R(int i4, String str);

    @Override // r3.k
    public final native ArrayList V0(int i4, String str);

    @Override // r3.k
    public final native void j2(IBinder iBinder);

    @Override // r3.k
    public final native void k3(int i4, Bitmap bitmap);

    @Override // r3.k
    public final native IBinder l0(int i4);

    @Override // r3.k
    public final native void r1(int i4, IBinder iBinder);

    @Override // r3.k
    public final native ArrayList x2(int i4);
}
