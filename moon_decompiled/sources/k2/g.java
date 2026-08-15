package k2;

import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "freeBuffer", value = {})
    private static v1.f<Void> f694a;

    static {
        j.e.r(g.class, Parcel.class);
    }

    public static void a(Parcel parcel) {
        v1.f<Void> fVar = f694a;
        if (fVar != null) {
            fVar.a(parcel, j.e.f514r);
        }
    }
}
