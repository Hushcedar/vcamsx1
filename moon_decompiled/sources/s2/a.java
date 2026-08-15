package s2;

import java.util.Collection;
import java.util.HashSet;
import v1.e;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "", value = {Collection.class})
    private static v1.a<Object> f1477a;

    static {
        j.e.q(a.class, "android.util.ArraySet");
    }

    public static Object a(HashSet hashSet) {
        v1.a<Object> aVar = f1477a;
        if (aVar != null) {
            return aVar.a(new Object[]{hashSet});
        }
        return null;
    }
}
