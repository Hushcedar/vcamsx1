package u1;

import android.content.Context;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class o implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1666a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Context f1667b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String f1668c;

    static {
        perationCompat.init0(230);
    }

    public /* synthetic */ o(int i4, Context context, String str) {
        this.f1666a = i4;
        this.f1667b = context;
        this.f1668c = str;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
