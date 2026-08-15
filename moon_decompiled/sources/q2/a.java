package q2;

import android.os.Parcelable;
import j.e;
import v1.b;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1292a = e.q(a.class, "android.telecom.PhoneAccountHandle");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @b(name = "CREATOR")
    private static g<Parcelable.Creator> f1293b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static Parcelable.Creator f1294c;

    public static Parcelable.Creator a() {
        g<Parcelable.Creator> gVar;
        if (f1294c == null && (gVar = f1293b) != null) {
            f1294c = gVar.a();
        }
        return f1294c;
    }
}
