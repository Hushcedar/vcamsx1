package k3;

import android.content.Intent;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ v f844a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Intent f845b;

    static {
        perationCompat.init0(285);
    }

    public /* synthetic */ m(String str, v vVar, Intent intent) {
        this.f844a = vVar;
        this.f845b = intent;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
