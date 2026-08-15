package i3;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class g extends i3.a {
    public static final Parcelable.Creator<g> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f463a;

    public class a implements Parcelable.Creator<g> {
        static {
            perationCompat.init0(549);
        }

        @Override // android.os.Parcelable.Creator
        public final native g createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native g[] newArray(int i4);
    }

    static {
        perationCompat.init0(127);
        CREATOR = new a();
    }

    public g() {
    }

    public g(Parcel parcel) {
        this.f463a = parcel.readInt() != 0;
    }

    @Override // i3.a, i3.k
    public final native void a();

    @Override // i3.f
    public final native void b(d.a aVar, IBinder iBinder);

    public final native boolean equals(Object obj);

    public final native int hashCode();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
