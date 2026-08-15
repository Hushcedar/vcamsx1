package i2;

import j.e;
import v1.c;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f441a = e.q(a.class, "android.location.GeocoderParams");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mPackageName")
    private static c<String> f442b;

    public static void a(Object obj, String str) {
        c<String> cVar = f442b;
        if (cVar != null) {
            cVar.b(obj, str);
        }
    }
}
