package b2;

import android.content.IntentFilter;
import android.os.Build;
import android.util.ArraySet;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mActions")
    private static v1.c<Object> f74a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "mCategories")
    private static v1.c<ArrayList<String>> f75b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "hasDataAuthority", value = {IntentFilter.AuthorityEntry.class})
    private static v1.f<Boolean> f76c;

    static {
        j.e.r(g.class, IntentFilter.class);
    }

    public static boolean a() {
        return f74a != null;
    }

    public static boolean b(IntentFilter intentFilter, IntentFilter.AuthorityEntry authorityEntry) {
        v1.f<Boolean> fVar = f76c;
        if (fVar != null) {
            return fVar.a(intentFilter, new Object[]{authorityEntry}).booleanValue();
        }
        return false;
    }

    public static ArrayList<String> c(IntentFilter intentFilter) {
        Object objA;
        v1.c<Object> cVar = f74a;
        if (cVar == null || (objA = cVar.a(intentFilter)) == null) {
            return null;
        }
        if (objA instanceof ArrayList) {
            return (ArrayList) objA;
        }
        if (Build.VERSION.SDK_INT < 33 || !(objA instanceof ArraySet)) {
            return null;
        }
        return new ArrayList<>((ArraySet) objA);
    }

    public static void d(IntentFilter intentFilter, ArrayList<String> arrayList) {
        v1.c<Object> cVar = f74a;
        if (cVar != null) {
            Class<?> type = cVar.f1714a.getType();
            if (type == ArrayList.class) {
                f74a.b(intentFilter, arrayList);
            } else {
                if (Build.VERSION.SDK_INT < 29 || type != ArraySet.class) {
                    return;
                }
                f74a.b(intentFilter, new ArraySet(arrayList));
            }
        }
    }

    public static void e(IntentFilter intentFilter) {
        v1.c<ArrayList<String>> cVar = f75b;
        if (cVar != null) {
            cVar.b(intentFilter, null);
        }
    }
}
