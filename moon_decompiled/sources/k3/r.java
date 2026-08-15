package k3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class r implements Parcelable {
    public static final Parcelable.Creator<r> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f895a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f896b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final boolean f897c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f898d;

    public class a implements Parcelable.Creator<r> {
        static {
            perationCompat.init0(434);
        }

        @Override // android.os.Parcelable.Creator
        public final native r createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native r[] newArray(int i4);
    }

    static {
        perationCompat.init0(291);
        CREATOR = new a();
    }

    public r(String str, int i4, int i5, boolean z3) {
        this.f895a = str;
        this.f896b = i4;
        this.f897c = z3;
        this.f898d = i5;
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
