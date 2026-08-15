package t1;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Parcelable {
    public static final Parcelable.Creator<b> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1557a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f1558b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f1559c;

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

    public b(int i4, String str, String str2) {
        this.f1557a = str;
        this.f1558b = str2;
        this.f1559c = i4;
    }

    public b(Parcel parcel) {
        this.f1557a = parcel.readString();
        this.f1558b = parcel.readString();
        this.f1559c = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof b)) {
            return false;
        }
        b bVar = (b) obj;
        return this.f1559c == bVar.f1559c && this.f1557a.equals(bVar.f1557a) && this.f1558b.equals(bVar.f1558b);
    }

    public final int hashCode() {
        return Objects.hash(this.f1557a, this.f1558b, Integer.valueOf(this.f1559c));
    }

    public final String toString() {
        return String.format("AppNotificationChannel{%s, %s, %s}", this.f1557a, this.f1558b, Integer.valueOf(this.f1559c));
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i4) {
        parcel.writeString(this.f1557a);
        parcel.writeString(this.f1558b);
        parcel.writeInt(this.f1559c);
    }
}
