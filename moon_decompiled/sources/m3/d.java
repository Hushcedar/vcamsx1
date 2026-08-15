package m3;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final e f1079a;

    public d(Context context) {
        if (e.f1082p == null) {
            e.f1082p = new e(context, p.a.c());
        }
        if (e.f1082p == null) {
            throw new IllegalStateException("not initialized");
        }
        this.f1079a = e.f1082p;
    }
}
