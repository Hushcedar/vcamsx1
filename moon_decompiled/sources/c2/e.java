package c2;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f107a = j.e.q(e.class, "android.content.pm.PackageBackwardCompatibility");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.d(name = "modifySharedLibraries", value = {@v1.i(strings = {"android.content.pm.PackageParser$Package"}, type = 1)})
    private static v1.f<Void> f108b;

    public static void a(Object obj) {
        v1.f<Void> fVar = f108b;
        if (fVar == null || obj == null) {
            return;
        }
        fVar.a(null, new Object[]{obj});
    }
}
