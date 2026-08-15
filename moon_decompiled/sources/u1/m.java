package u1;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class m<T extends Parcelable> implements j<T[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Parcelable.Creator<T> f1660a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f1661b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1662c = 0;

    static {
        perationCompat.init0(240);
    }

    public m(Parcelable.Creator<T> creator) {
        this.f1660a = creator;
    }

    public m(Class<?> cls) {
        Field fieldA = v3.j.a(cls, "CREATOR");
        if (fieldA != null) {
            fieldA.setAccessible(true);
        }
        Object obj = null;
        try {
            obj = fieldA.get(null);
        } catch (Exception unused) {
        }
        this.f1660a = (Parcelable.Creator) obj;
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
