package i3;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class l implements Parcelable {
    public static final Parcelable.Creator<l> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f476a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Intent f477b;

    public class a implements Parcelable.Creator<l> {
        static {
            perationCompat.init0(316);
        }

        @Override // android.os.Parcelable.Creator
        public final native l createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native l[] newArray(int i4);
    }

    static {
        perationCompat.init0(138);
        CREATOR = new a();
    }

    public l(Intent intent, String str) {
        this.f477b = intent;
        this.f476a = str;
    }

    public l(Parcel parcel) {
        this.f477b = (Intent) Intent.CREATOR.createFromParcel(parcel);
        this.f476a = parcel.readString();
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native boolean equals(Object obj);

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
