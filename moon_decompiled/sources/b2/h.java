package b2;

import android.content.IntentSender;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mTarget")
    private static v1.c<IInterface> f77a;

    static {
        j.e.q(h.class, IntentSender.class.getName());
    }

    public static IInterface a(IntentSender intentSender) {
        v1.c<IInterface> cVar = f77a;
        if (cVar != null) {
            return cVar.a(intentSender);
        }
        return null;
    }
}
