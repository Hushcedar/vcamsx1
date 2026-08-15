package k3;

import android.content.IntentFilter;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class j extends IntentFilter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final w f833a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f834b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f835c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f836d;

    static {
        perationCompat.init0(279);
    }

    public j(IntentFilter intentFilter, w wVar, String str, int i4, int i5) {
        super(intentFilter);
        this.f833a = wVar;
        this.f834b = str;
        this.f835c = i4;
        this.f836d = i5;
    }

    public final native String toString();
}
