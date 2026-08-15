package i3;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class i extends f {
    public static final Parcelable.Creator<i> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public List<l> f472a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f473b;

    public class a implements Parcelable.Creator<i> {
        static {
            perationCompat.init0(470);
        }

        @Override // android.os.Parcelable.Creator
        public final native i createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native i[] newArray(int i4);
    }

    static {
        perationCompat.init0(132);
        CREATOR = new a();
    }

    public i() {
    }

    public i(Parcel parcel) {
        this.f473b = parcel.readInt() != 0;
        this.f472a = parcel.createTypedArrayList(l.CREATOR);
    }

    @Override // i3.k
    public final native void a();

    @Override // i3.f
    public final native void b(d.a aVar, IBinder iBinder);

    public final native boolean equals(Object obj);

    public final native int hashCode();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
