package x2;

import android.os.Parcelable;
import j.e;
import v1.b;
import v1.g;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f1861a = e.q(a.class, "com.android.bluetooth.x.com.android.modules.utils.SynchronousResultReceiver");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @b(name = "CREATOR")
    private static g<Parcelable.Creator<Parcelable>> f1862b;

    public static Parcelable.Creator<Parcelable> a() {
        g<Parcelable.Creator<Parcelable>> gVar = f1862b;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }
}
