package w3;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class b extends w3.a {
    public static final Parcelable.Creator<b> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Parcelable f1758a;

    public class a implements Parcelable.Creator<b> {
        @Override // android.os.Parcelable.Creator
        public final b createFromParcel(Parcel parcel) {
            return new b(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final b[] newArray(int i4) {
            return new b[i4];
        }
    }

    public b(Parcel parcel) {
        this.f1758a = parcel.readParcelable(b.class.getClassLoader());
    }

    public b(Parcelable parcelable) {
        this.f1758a = parcelable;
    }

    @Override // w3.a
    public final Object a() {
        return this.f1758a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i4) {
        parcel.writeParcelable(this.f1758a, 1);
    }
}
