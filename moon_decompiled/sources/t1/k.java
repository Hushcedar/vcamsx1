package t1;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class k implements Parcelable {
    public static final Parcelable.Creator<k> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1574a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1575b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f1576c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Intent f1577d;

    public class a implements Parcelable.Creator<k> {
        @Override // android.os.Parcelable.Creator
        public final k createFromParcel(Parcel parcel) {
            return new k(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final k[] newArray(int i4) {
            return new k[i4];
        }
    }

    public k(Intent intent, String str, int i4, int i5) {
        this.f1574a = str;
        this.f1575b = i4;
        this.f1576c = i5;
        this.f1577d = intent;
    }

    public k(Parcel parcel) {
        this.f1574a = parcel.readString();
        this.f1575b = parcel.readInt();
        this.f1576c = parcel.readInt();
        this.f1577d = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        Intent intent = kVar.f1577d;
        Intent intent2 = this.f1577d;
        return (intent2 == null ? intent == null : intent2.filterEquals(intent)) && Objects.equals(this.f1574a, kVar.f1574a) && this.f1576c == kVar.f1576c && this.f1575b == kVar.f1575b;
    }

    public final int hashCode() {
        int iHashCode = Objects.hashCode(this.f1574a) + ((((527 + this.f1575b) * 31) + this.f1576c) * 31);
        Intent intent = this.f1577d;
        return intent != null ? (iHashCode * 31) + intent.filterHashCode() : iHashCode;
    }

    public final String toString() {
        return "ResultInfo{who=" + this.f1574a + ", request=" + this.f1575b + ", result=" + this.f1576c + ", data=" + this.f1577d + "}";
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i4) {
        parcel.writeString(this.f1574a);
        parcel.writeInt(this.f1575b);
        parcel.writeInt(this.f1576c);
        Intent intent = this.f1577d;
        if (intent == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            intent.writeToParcel(parcel, 0);
        }
    }
}
