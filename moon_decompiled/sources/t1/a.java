package t1;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Parcelable {
    public static final Parcelable.Creator<a> CREATOR = new C0084a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1553a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f1554b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f1555c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f1556d;

    /* JADX INFO: renamed from: t1.a$a, reason: collision with other inner class name */
    public class C0084a implements Parcelable.Creator<a> {
        @Override // android.os.Parcelable.Creator
        public final a createFromParcel(Parcel parcel) {
            return new a(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final a[] newArray(int i4) {
            return new a[i4];
        }
    }

    public a(int i4, String str, String str2, int i5) {
        this.f1553a = str;
        this.f1554b = str2;
        this.f1555c = i4;
        this.f1556d = i5;
    }

    public a(Parcel parcel) {
        this.f1553a = parcel.readString();
        this.f1554b = parcel.readString();
        this.f1555c = parcel.readInt();
        this.f1556d = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        a aVar = (a) obj;
        return this.f1555c == aVar.f1555c && this.f1556d == aVar.f1556d && TextUtils.equals(this.f1553a, aVar.f1553a) && TextUtils.equals(this.f1554b, aVar.f1554b);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i4) {
        parcel.writeString(this.f1553a);
        parcel.writeString(this.f1554b);
        parcel.writeInt(this.f1555c);
        parcel.writeInt(this.f1556d);
    }
}
