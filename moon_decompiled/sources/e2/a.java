package e2;

import android.content.res.AssetManager;
import v1.e;
import v1.f;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "", value = {})
    private static v1.a<AssetManager> f327a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @e(name = "addAssetPath", value = {String.class})
    private static f<Integer> f328b;

    static {
        j.e.r(a.class, AssetManager.class);
    }

    public static void a(AssetManager assetManager, String str) {
        f<Integer> fVar = f328b;
        if (fVar != null) {
            fVar.a(assetManager, new Object[]{str}).intValue();
        }
    }

    public static AssetManager b() {
        v1.a<AssetManager> aVar = f327a;
        if (aVar != null) {
            return aVar.a(j.e.f514r);
        }
        return null;
    }
}
