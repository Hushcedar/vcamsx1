package i3;

import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class d extends f {
    public static final Parcelable.Creator<d> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Intent f453a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f454b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f455c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f456d;

    public class a implements Parcelable.Creator<d> {
        static {
            perationCompat.init0(657);
        }

        @Override // android.os.Parcelable.Creator
        public final native d createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native d[] newArray(int i4);
    }

    static {
        perationCompat.init0(68);
        CREATOR = new a();
    }

    public d() {
    }

    public d(Parcel parcel) {
        Intent intent = parcel.readInt() == 1 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
        String string = parcel.readString();
        int i4 = parcel.readInt();
        int i5 = parcel.readInt();
        this.f453a = intent;
        this.f454b = string;
        this.f455c = i4;
        this.f456d = i5;
    }

    @Override // i3.k
    public final native void a();

    @Override // i3.f
    public final native void b(d.a aVar, IBinder iBinder);

    @Override // i3.f
    public final native void c();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
