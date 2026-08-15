package z1;

import j.e;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1906a = e.q(c.class, "android.app.servertransaction.TopResumedActivityChangeItem");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mOnTop")
    private static v1.c<Boolean> f1907b;

    public static boolean a(Object obj) {
        v1.c<Boolean> cVar = f1907b;
        if (cVar == null || obj == null) {
            return false;
        }
        return cVar.a(obj).booleanValue();
    }
}
