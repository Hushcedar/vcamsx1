package w3;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class c extends w3.a {
    public static final Parcelable.Creator<c> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1759a;

    public class a implements Parcelable.Creator<c> {
        @Override // android.os.Parcelable.Creator
        public final c createFromParcel(Parcel parcel) {
            return new c(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final c[] newArray(int i4) {
            return new c[i4];
        }
    }

    public c(Parcel parcel) {
        this.f1759a = parcel.readString();
    }

    public c(String str) {
        this.f1759a = str;
    }

    @Override // w3.a
    public final Object a() {
        return this.f1759a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i4) {
        parcel.writeString(this.f1759a);
    }
}
