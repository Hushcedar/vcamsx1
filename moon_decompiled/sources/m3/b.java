package m3;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Parcelable {
    public static final Parcelable.Creator<b> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Account f1069a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f1070b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Bundle f1071c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final long f1072d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final long f1073e;

    public class a implements Parcelable.Creator<b> {
        static {
            perationCompat.init0(111);
        }

        @Override // android.os.Parcelable.Creator
        public final native b createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native b[] newArray(int i4);
    }

    static {
        perationCompat.init0(358);
        CREATOR = new a();
    }

    public b(Account account, String str, Bundle bundle, long j4, long j5) {
        this.f1069a = account;
        this.f1070b = str;
        this.f1071c = new Bundle(bundle);
        this.f1072d = j4;
        this.f1073e = j5;
    }

    public b(Parcel parcel) {
        this.f1069a = (Account) parcel.readParcelable(null);
        this.f1070b = parcel.readString();
        this.f1071c = parcel.readBundle();
        this.f1072d = parcel.readLong();
        this.f1073e = parcel.readLong();
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native boolean equals(Object obj);

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
