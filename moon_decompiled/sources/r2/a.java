package r2;

import android.os.Parcelable;
import j.e;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1303a = e.q(a.class, "android.telephony.PhoneNumberRange");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static g<Parcelable.Creator> f1304b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static Parcelable.Creator f1305c;

    public static Parcelable.Creator a() {
        g<Parcelable.Creator> gVar;
        if (f1305c == null && (gVar = f1304b) != null) {
            f1305c = gVar.a();
        }
        return f1305c;
    }
}
