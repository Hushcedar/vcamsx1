package c2;

import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "", value = {String.class, String.class, List.class, String.class, long.class, int.class, VersionedPackage.class, List.class, List.class})
    public static v1.a<SharedLibraryInfo> f221a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "getAllCodePaths", value = {})
    private static v1.f<List> f222b;

    static {
        j.e.q(n.class, "android.content.pm.SharedLibraryInfo");
    }

    public static List a(Object obj) {
        v1.f<List> fVar = f222b;
        if (fVar != null) {
            return fVar.a(obj, null);
        }
        return null;
    }
}
