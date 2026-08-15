package k2;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f696a = j.e.q(i.class, "android.os.RemoteCallback");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static v1.g<Parcelable.Creator<Parcelable>> f697b;

    public static Parcelable.Creator<Parcelable> a() {
        v1.g<Parcelable.Creator<Parcelable>> gVar = f697b;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }
}
