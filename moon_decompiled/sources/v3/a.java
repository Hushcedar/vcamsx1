package v3;

import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import i3.c;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static boolean a(ActivityInfo activityInfo) {
        TypedArray typedArray;
        if (activityInfo == null || activityInfo.getThemeResource() == 0) {
            return false;
        }
        try {
            c.a aVarA = i3.c.f447c.a(activityInfo.packageName, activityInfo.applicationInfo.sourceDir, activityInfo.getThemeResource(), y2.c.f1891h.a());
            if (aVarA == null || (typedArray = aVarA.f450a) == null) {
                return false;
            }
            return typedArray.getBoolean(y2.c.f1893j.a().intValue(), false) || typedArray.getBoolean(y2.c.f1892i.a().intValue(), false) || typedArray.getBoolean(y2.c.f1894k.a().intValue(), false);
        } catch (Exception unused) {
            return false;
        }
    }
}
