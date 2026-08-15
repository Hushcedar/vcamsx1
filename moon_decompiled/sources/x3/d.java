package x3;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.util.HashSet;
import java.util.List;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1866c = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Handler f1867a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final HashSet<b> f1868b;

    public class a extends k<d> {
        @Override // v3.k
        public final d a() {
            return new d();
        }
    }

    public d() {
        HandlerThread handlerThread = new HandlerThread("ipc");
        this.f1868b = new HashSet<>();
        handlerThread.start();
        this.f1867a = new Handler(handlerThread.getLooper(), new Handler.Callback() { // from class: x3.c
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                d dVar = this.f1865a;
                dVar.getClass();
                int i4 = message.what;
                HashSet<b> hashSet = dVar.f1868b;
                if (i4 == 0) {
                    hashSet.add((b) message.obj);
                } else if (i4 == 1 || i4 == 2) {
                    hashSet.remove((b) message.obj);
                }
                return true;
            }
        });
    }

    public static e a(List list) {
        e eVar = new e(list, 0);
        f1866c.b().b(eVar);
        return eVar;
    }

    public final void b(e eVar) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 0;
        messageObtain.obj = eVar;
        Handler handler = this.f1867a;
        handler.sendMessage(messageObtain);
        Message messageObtain2 = Message.obtain();
        messageObtain2.what = 2;
        messageObtain2.obj = eVar;
        handler.sendMessageDelayed(messageObtain2, 15000L);
    }
}
