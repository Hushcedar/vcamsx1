package w2;

import android.widget.TextView;
import j.e;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mSingleLine")
    private static c<Boolean> f1757a;

    static {
        e.r(b.class, TextView.class);
    }

    public static boolean a(TextView textView) {
        c<Boolean> cVar = f1757a;
        if (cVar != null) {
            return cVar.a(textView).booleanValue();
        }
        return false;
    }
}
