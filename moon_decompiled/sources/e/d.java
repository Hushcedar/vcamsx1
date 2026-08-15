package e;

import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import f.b;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Parcelable {
    public static final Parcelable.Creator<d> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ProviderInfo f316a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f317b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IBinder f318c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public f.b f319d;

    public class a implements Parcelable.Creator<d> {
        static {
            perationCompat.init0(572);
        }

        @Override // android.os.Parcelable.Creator
        public final native d createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native d[] newArray(int i4);
    }

    static {
        perationCompat.init0(215);
        CREATOR = new a();
    }

    public d(ProviderInfo providerInfo, IBinder iBinder, int i4) {
        this.f316a = providerInfo;
        this.f317b = i4;
        this.f318c = iBinder;
    }

    public d(Parcel parcel) {
        this.f316a = (ProviderInfo) parcel.readParcelable(ProviderInfo.class.getClassLoader());
        this.f317b = parcel.readInt();
        this.f318c = parcel.readStrongBinder();
        this.f319d = b.a.E3(parcel.readStrongBinder());
    }

    public final native IBinder a();

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
