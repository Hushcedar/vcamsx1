package i3;

import android.os.HandlerThread;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class m extends HandlerThread {
    static {
        perationCompat.init0(142);
    }

    public m(String str) {
        super(str, -2);
    }

    @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
    public final native void run();
}
