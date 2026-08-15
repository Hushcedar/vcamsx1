package x3;

import android.os.Handler;
import android.os.Message;
import x3.b;

/* JADX INFO: loaded from: classes.dex */
public abstract class a extends b.a {
    @Override // x3.b
    public final void close() {
        Handler handler = d.f1866c.b().f1867a;
        handler.removeMessages(2, this);
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        messageObtain.obj = this;
        handler.sendMessage(messageObtain);
    }
}
