package u1;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class f<T extends Parcelable> implements j<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Parcelable.Creator<T> f1652a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f1653b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1654c = 0;

    static {
        perationCompat.init0(223);
    }

    public f(Parcelable.Creator<T> creator) {
        this.f1652a = creator;
    }

    public f(Class<?> cls) {
        Field fieldA = v3.j.a(cls, "CREATOR");
        if (fieldA != null) {
            fieldA.setAccessible(true);
        }
        Object obj = null;
        try {
            obj = fieldA.get(null);
        } catch (Exception unused) {
        }
        this.f1652a = (Parcelable.Creator) obj;
    }

    @Override // u1.j
    public final native void a();

    @Override // u1.j
    public final native int b();

    @Override // u1.j
    public final native Object c(Parcel parcel);

    @Override // u1.j
    public final native void d(int i4, Parcel parcel, Object obj);

    public f(String str) {
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
        this.f1652a = (Parcelable.Creator) obj;
    }
}
