package m2;

import android.provider.MediaStore;
import java.io.File;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "getVolumePath", value = {String.class})
    private static h f1041a;

    static {
        j.e.r(b.class, MediaStore.class);
    }

    public static File a() {
        h hVar = f1041a;
        if (hVar == null) {
            return null;
        }
        return (File) hVar.a(new Object[]{"external_primary"});
    }
}
