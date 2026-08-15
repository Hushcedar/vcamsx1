package r3;

import android.content.pm.ComponentInfo;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class u implements Parcelable, v3.f {
    public static final Parcelable.Creator<u> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f1435a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final boolean f1436b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final boolean f1437c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final boolean f1438d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f1439e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final boolean f1440f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public int f1441g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1442h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public HashSet<String> f1443i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public HashSet<String> f1444j;

    public class a implements Parcelable.Creator<u> {
        static {
            perationCompat.init0(667);
        }

        @Override // android.os.Parcelable.Creator
        public final native u createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native u[] newArray(int i4);
    }

    static {
        perationCompat.init0(719);
        CREATOR = new a();
    }

    public u() {
        this.f1441g = -1;
        this.f1435a = false;
        this.f1436b = false;
        this.f1437c = true;
        this.f1438d = false;
        this.f1439e = 0;
        this.f1440f = false;
        this.f1443i = new HashSet<>();
        this.f1444j = new HashSet<>();
    }

    public u(Parcel parcel) {
        this.f1441g = -1;
        this.f1435a = parcel.readByte() != 0;
        this.f1436b = parcel.readByte() != 0;
        this.f1437c = parcel.readByte() != 0;
        this.f1438d = parcel.readByte() != 0;
        this.f1439e = parcel.readInt();
        this.f1440f = parcel.readByte() != 0;
        this.f1441g = parcel.readInt();
        this.f1442h = parcel.readString();
        this.f1443i = new HashSet<>();
        int i4 = parcel.readInt();
        for (int i5 = 0; i5 < i4; i5++) {
            this.f1443i.add(parcel.readString());
        }
        this.f1444j = new HashSet<>();
        int i6 = parcel.readInt();
        for (int i7 = 0; i7 < i6; i7++) {
            this.f1444j.add(parcel.readString());
        }
    }

    public u(JSONObject jSONObject) throws JSONException {
        this.f1441g = -1;
        this.f1435a = jSONObject.optBoolean("installed");
        this.f1436b = jSONObject.optBoolean("stopped");
        this.f1437c = jSONObject.optBoolean("notLaunched");
        this.f1438d = jSONObject.optBoolean("hidden");
        this.f1439e = jSONObject.optInt("enabled");
        this.f1440f = jSONObject.optBoolean("instantApp");
        this.f1441g = jSONObject.optInt("categoryHint");
        this.f1442h = jSONObject.optString("lastDisableAppCaller");
        this.f1443i = new HashSet<>();
        JSONArray jSONArray = jSONObject.getJSONArray("disabledComponents");
        for (int i4 = 0; i4 < jSONArray.length(); i4++) {
            this.f1443i.add(jSONArray.optString(i4));
        }
        this.f1444j = new HashSet<>();
        JSONArray jSONArray2 = jSONObject.getJSONArray("enabledComponents");
        for (int i5 = 0; i5 < jSONArray2.length(); i5++) {
            this.f1444j.add(jSONArray2.optString(i5));
        }
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);

    public final native boolean b(ComponentInfo componentInfo, int i4);

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native boolean equals(Object obj);

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
