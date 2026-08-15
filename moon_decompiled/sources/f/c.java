package f;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Parcelable {
    public static final Parcelable.Creator<c> CREATOR;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final c f335d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final long f336a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f337b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Set<String> f338c;

    public class a implements Parcelable.Creator<c> {
        static {
            perationCompat.init0(521);
        }

        @Override // android.os.Parcelable.Creator
        public final native c createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native c[] newArray(int i4);
    }

    static {
        perationCompat.init0(180);
        f335d = new c(0, null, new HashSet());
        CREATOR = new a();
    }

    public c(int i4, String str, HashSet hashSet) {
        this.f336a = i4;
        this.f337b = str;
        this.f338c = hashSet;
    }

    public c(Parcel parcel) {
        this.f336a = parcel.readLong();
        this.f337b = parcel.readString();
        this.f338c = new HashSet();
        int i4 = parcel.readInt();
        for (int i5 = 0; i5 < i4; i5++) {
            this.f338c.add(parcel.readString());
        }
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
