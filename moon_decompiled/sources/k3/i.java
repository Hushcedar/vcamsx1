package k3;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class i implements Parcelable {
    public static final Parcelable.Creator<i> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IBinder f829a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IBinder f830b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f831c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f832d;

    public class a implements Parcelable.Creator<i> {
        static {
            perationCompat.init0(7);
        }

        @Override // android.os.Parcelable.Creator
        public final native i createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native i[] newArray(int i4);
    }

    static {
        perationCompat.init0(281);
        CREATOR = new a();
    }

    public i() {
    }

    public i(Parcel parcel) {
        this.f829a = parcel.readStrongBinder();
        this.f830b = parcel.readStrongBinder();
        this.f831c = parcel.readInt();
        this.f832d = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
