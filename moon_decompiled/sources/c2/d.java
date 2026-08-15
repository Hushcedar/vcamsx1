package c2;

import android.content.pm.InstallSourceInfo;
import android.content.pm.SigningInfo;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "", value = {String.class, SigningInfo.class, String.class, String.class})
    private static v1.a<InstallSourceInfo> f106a;

    static {
        j.e.q(d.class, "android.content.pm.InstallSourceInfo");
    }

    public static InstallSourceInfo a(String str, SigningInfo signingInfo, String str2, String str3) {
        v1.a<InstallSourceInfo> aVar = f106a;
        if (aVar != null) {
            return aVar.a(new Object[]{str, signingInfo, str2, str3});
        }
        return null;
    }
}
