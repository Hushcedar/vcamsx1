package u1;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.lang.reflect.Field;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class n<T extends Parcelable> implements j<List<T>> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Parcelable.Creator<T> f1663a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f1664b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1665c = 0;

    static {
        perationCompat.init0(238);
    }

    public n(Parcelable.Creator<T> creator) {
        this.f1663a = creator;
    }

    public n(String str) {
        Field fieldA;
        Object obj = null;
        try {
            fieldA = v3.j.a(Class.forName(str), "CREATOR");
        } catch (ClassNotFoundException unused) {
            fieldA = null;
        }
        if (fieldA != null) {
            fieldA.setAccessible(true);
        }
        try {
            obj = fieldA.get(null);
        } catch (Exception unused2) {
        }
        this.f1663a = (Parcelable.Creator) obj;
    }

    @Override // u1.j
    public final native void a();

    @Override // u1.j
    public final native int b();

    @Override // u1.j
    public final native Object c(Parcel parcel);

    @Override // u1.j
    public final native void d(int i4, Parcel parcel, Object obj);
}
