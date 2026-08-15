package r3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class y implements Parcelable, v3.f {
    public static final Parcelable.Creator<y> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1454a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1455b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1456c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final String f1457d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f1458e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final long f1459f;

    public class a implements Parcelable.Creator<y> {
        static {
            perationCompat.init0(97);
        }

        @Override // android.os.Parcelable.Creator
        public final native y createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native y[] newArray(int i4);
    }

    static {
        perationCompat.init0(731);
        CREATOR = new a();
    }

    public y() {
    }

    public y(int i4, String str) {
        this.f1454a = i4;
        this.f1456c = str;
        this.f1458e = 16;
        this.f1457d = null;
    }

    public y(Parcel parcel) {
        this.f1454a = parcel.readInt();
        this.f1456c = parcel.readString();
        this.f1457d = parcel.readString();
        this.f1458e = parcel.readInt();
        this.f1455b = parcel.readInt();
        this.f1459f = parcel.readLong();
    }

    public y(JSONObject jSONObject) {
        this.f1454a = jSONObject.optInt("id");
        this.f1456c = jSONObject.optString("name");
        this.f1457d = jSONObject.optString("icon");
        this.f1458e = jSONObject.optInt("flags");
        this.f1455b = jSONObject.optInt("serialNumber");
        this.f1459f = jSONObject.optInt("creationTime");
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);

    public final native boolean b();

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
