package f;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import f.b;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Parcelable {
    public static final Parcelable.Creator<a> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f330a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f331b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final b f332c;

    /* JADX INFO: renamed from: f.a$a, reason: collision with other inner class name */
    public class C0017a implements Parcelable.Creator<a> {
        static {
            perationCompat.init0(379);
        }

        @Override // android.os.Parcelable.Creator
        public final native a createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native a[] newArray(int i4);
    }

    static {
        perationCompat.init0(178);
        CREATOR = new C0017a();
    }

    public a(int i4, int i5, e.b bVar) {
        this.f330a = i4;
        this.f331b = i5;
        this.f332c = bVar;
    }

    public a(Parcel parcel) {
        this.f330a = parcel.readInt();
        this.f331b = parcel.readInt();
        this.f332c = parcel.readInt() != 0 ? b.a.E3(parcel.readStrongBinder()) : null;
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
