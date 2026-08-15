package f;

import android.content.ContentProviderClient;
import android.content.Context;
import android.os.Bundle;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f353a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f354b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ContentProviderClient f355c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final boolean f356d;

    static {
        perationCompat.init0(181);
    }

    public f(Context context, String str, boolean z3) {
        this.f356d = true;
        if (context == null) {
            throw new IllegalArgumentException("context null");
        }
        this.f353a = context;
        this.f354b = str;
        this.f356d = z3;
    }

    public final native Bundle a(String str, Bundle bundle);
}
