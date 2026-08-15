package e;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class e implements Parcelable {
    public static final Parcelable.Creator<e> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final boolean f320a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f321b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f322c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Intent f323d;

    public class a implements Parcelable.Creator<e> {
        static {
            perationCompat.init0(545);
        }

        @Override // android.os.Parcelable.Creator
        public final native e createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native e[] newArray(int i4);
    }

    static {
        perationCompat.init0(213);
        CREATOR = new a();
    }

    public e(Parcel parcel) {
        this.f320a = parcel.readInt() != 0;
        this.f321b = parcel.readInt();
        this.f322c = parcel.readInt();
        this.f323d = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
    }

    public e(boolean z3, int i4, int i5, Intent intent) {
        this.f320a = z3;
        this.f321b = i4;
        this.f322c = i5;
        this.f323d = intent;
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
