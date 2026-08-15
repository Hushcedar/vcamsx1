package o3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import org.json.JSONObject;
import v3.f;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Parcelable, f {
    public static final Parcelable.Creator<a> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1160a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1161b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f1162c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f1163d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public String f1164e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public String f1165f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final String f1166g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public boolean f1167h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final String f1168i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final boolean f1169j;

    /* JADX INFO: renamed from: o3.a$a, reason: collision with other inner class name */
    public class C0068a implements Parcelable.Creator<a> {
        static {
            perationCompat.init0(339);
        }

        @Override // android.os.Parcelable.Creator
        public final native a createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native a[] newArray(int i4);
    }

    static {
        perationCompat.init0(523);
        CREATOR = new C0068a();
    }

    public a() {
        throw null;
    }

    public a(int i4, String str, boolean z3, boolean z4) {
        this.f1160a = i4;
        this.f1167h = z3;
        this.f1168i = str;
        this.f1169j = z4;
    }

    public a(Parcel parcel) {
        this.f1160a = parcel.readInt();
        this.f1161b = parcel.readString();
        this.f1162c = parcel.readString();
        this.f1163d = parcel.readString();
        this.f1164e = parcel.readString();
        this.f1165f = parcel.readString();
        this.f1166g = parcel.readString();
        this.f1167h = parcel.readInt() != 0;
        this.f1168i = parcel.readString();
        this.f1169j = parcel.readInt() != 0;
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
