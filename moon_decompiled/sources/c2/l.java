package c2;

import android.content.pm.ResolveInfo;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "handleAllWebDataURI")
    private static v1.c<Boolean> f219a;

    static {
        j.e.r(l.class, ResolveInfo.class);
    }

    public static void a(ResolveInfo resolveInfo, boolean z3) {
        v1.c<Boolean> cVar = f219a;
        if (cVar != null) {
            cVar.b(resolveInfo, Boolean.valueOf(z3));
        }
    }
}
