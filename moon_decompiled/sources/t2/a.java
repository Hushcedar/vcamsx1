package t2;

import android.view.View;
import v1.e;
import v1.f;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "setTagInternal", value = {int.class, Object.class})
    private static f<Void> f1578a;

    static {
        j.e.r(a.class, View.class);
    }

    public static void a(View view, int i4, Integer num) {
        f<Void> fVar = f1578a;
        if (fVar != null) {
            fVar.a(view, new Object[]{Integer.valueOf(i4), num});
        }
    }
}
