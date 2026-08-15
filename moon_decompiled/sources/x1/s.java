package x1;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class s {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1851a = j.e.q(s.class, "android.app.ProfilerInfo");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static v1.g<Parcelable.Creator<Parcelable>> f1852b;

    public static Parcelable.Creator<Parcelable> a() {
        v1.g<Parcelable.Creator<Parcelable>> gVar = f1852b;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }
}
