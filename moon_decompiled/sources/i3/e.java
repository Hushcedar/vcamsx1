package i3;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class e implements Parcelable, k {
    public static final Parcelable.Creator<e> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f457a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public boolean f458b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ArrayList f459c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public i3.a f460d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public f.b f461e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public IBinder f462f;

    public class a implements Parcelable.Creator<e> {
        static {
            perationCompat.init0(634);
        }

        @Override // android.os.Parcelable.Creator
        public final native e createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native e[] newArray(int i4);
    }

    static {
        perationCompat.init0(123);
        CREATOR = new a();
    }

    public e() {
    }

    public e(Parcel parcel) {
        this.f457a = parcel.readInt() != 0;
        this.f458b = parcel.readInt() != 0;
        this.f461e = (f.b) parcel.readStrongBinder();
        if (parcel.readInt() != 0) {
            this.f462f = parcel.readStrongBinder();
        }
        this.f460d = (i3.a) parcel.readParcelable(e.class.getClassLoader());
        if (parcel.readInt() != 0) {
            for (Parcelable parcelable : parcel.readParcelableArray(f.class.getClassLoader())) {
                b((f) parcelable);
            }
        }
    }

    public static native e c(f.b bVar, IBinder iBinder, boolean z3);

    @Override // i3.k
    public final native void a();

    public final native void b(f fVar);

    public final native void d();

    @Override // android.os.Parcelable
    public final native int describeContents();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
