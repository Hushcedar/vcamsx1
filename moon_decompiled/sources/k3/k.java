package k3;

import android.net.LocalSocket;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class k extends Thread {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f837a;

    static {
        perationCompat.init0(283);
    }

    public k(String str) {
        this.f837a = str;
    }

    public static native void a(LocalSocket localSocket);

    @Override // java.lang.Thread, java.lang.Runnable
    public final native void run();
}
