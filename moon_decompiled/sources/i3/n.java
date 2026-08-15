package i3;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class n extends f {
    public static final Parcelable.Creator<n> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f478a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f479b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Intent f480c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Bundle f481d;

    public class a implements Parcelable.Creator<n> {
        static {
            perationCompat.init0(202);
        }

        @Override // android.os.Parcelable.Creator
        public final native n createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native n[] newArray(int i4);
    }

    static {
        perationCompat.init0(143);
        CREATOR = new a();
    }

    public n() {
    }

    public n(Parcel parcel) {
        int i4 = parcel.readInt();
        String string = parcel.readString();
        Intent intent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        Bundle bundle = parcel.readBundle(n.class.getClassLoader());
        this.f478a = i4;
        this.f479b = string;
        this.f480c = intent;
        this.f481d = bundle;
    }

    public static native n d(int i4, Intent intent, Bundle bundle, String str);

    @Override // i3.k
    public final native void a();

    @Override // i3.f
    public final native void b(d.a aVar, IBinder iBinder);

    @Override // i3.f
    public final native void c();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
