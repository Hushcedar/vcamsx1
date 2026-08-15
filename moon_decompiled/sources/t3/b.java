package t3;

import android.content.Context;
import android.os.Build;
import c2.j;
import k2.l;
import t1.c;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f1635a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1636b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Object f1637c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Object f1638d;

    public b(Context context) {
        boolean z3 = c.f1560a;
        this.f1635a = Build.VERSION.SDK_INT >= 33 ? d2.a.f278a.a() : j.a();
        this.f1636b = l.a();
    }
}
