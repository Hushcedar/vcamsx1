package v2;

import android.content.pm.PackageInfo;
import android.os.Parcelable;
import j.e;
import v1.b;
import v1.c;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @b(name = "packageInfo")
    public static c<PackageInfo> f1718a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @b(name = "status")
    public static c<Integer> f1719b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @b(name = "CREATOR")
    private static g<Parcelable.Creator> f1720c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static Parcelable.Creator f1721d;

    static {
        e.q(a.class, "android.webkit.WebViewProviderResponse");
    }

    public static Parcelable.Creator a() {
        if (f1721d == null) {
            f1721d = f1720c.a();
        }
        return f1721d;
    }
}
