package i3;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class b extends f {
    public static final Parcelable.Creator<b> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public List<t1.k> f446a;

    public class a implements Parcelable.Creator<b> {
        static {
            perationCompat.init0(742);
        }

        @Override // android.os.Parcelable.Creator
        public final native b createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native b[] newArray(int i4);
    }

    static {
        perationCompat.init0(64);
        CREATOR = new a();
    }

    public b() {
    }

    public b(Parcel parcel) {
        this.f446a = parcel.createTypedArrayList(t1.k.CREATOR);
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
