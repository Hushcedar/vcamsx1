package n3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import i.r;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Parcelable {
    public static final Parcelable.Creator<d> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f1133a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f1134b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1135c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f1136d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f1137e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f1138f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f1139g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public String f1140h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public String f1141i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public f.c f1142j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public r f1143k;

    public class a implements Parcelable.Creator<d> {
        static {
            perationCompat.init0(384);
        }

        @Override // android.os.Parcelable.Creator
        public final native d createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native d[] newArray(int i4);
    }

    static {
        perationCompat.init0(502);
        CREATOR = new a();
    }

    public d() {
        this.f1142j = f.c.f335d;
    }

    public d(Parcel parcel) {
        this.f1142j = f.c.f335d;
        this.f1133a = parcel.readInt();
        this.f1134b = parcel.readInt();
        this.f1135c = parcel.readInt();
        this.f1136d = parcel.readInt();
        this.f1137e = parcel.readInt() != 0;
        this.f1138f = parcel.readInt() != 0;
        this.f1139g = parcel.readInt() != 0;
        this.f1140h = parcel.readString();
        this.f1141i = parcel.readString();
        this.f1143k = r.a.E3(parcel.readStrongBinder());
        this.f1142j = new f.c(parcel);
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
