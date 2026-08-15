package r3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class n implements Parcelable, v3.f {
    public static final Parcelable.Creator<n> CREATOR;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static final String[] f1395p;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f1396a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f1397b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1398c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f1399d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public int f1400e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f1401f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f1402g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public boolean f1403h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public boolean f1404i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public boolean f1405j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public String f1406k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final String f1407l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public String f1408m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public String[] f1409n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public String[] f1410o;

    public class a implements Parcelable.Creator<n> {
        static {
            perationCompat.init0(372);
        }

        @Override // android.os.Parcelable.Creator
        public final native n createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native n[] newArray(int i4);
    }

    static {
        perationCompat.init0(721);
        f1395p = new String[0];
        CREATOR = new a();
    }

    public n(int i4, int i5, int i6, int i7, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, String str, String str2, String str3, String[] strArr, String[] strArr2) {
        this.f1396a = i4;
        this.f1397b = i5;
        this.f1398c = 0;
        this.f1399d = i6;
        this.f1400e = i7;
        this.f1401f = z3;
        this.f1402g = z4;
        this.f1403h = z5;
        this.f1404i = z6;
        this.f1405j = z7;
        this.f1406k = str;
        this.f1407l = str2;
        this.f1408m = str3;
        this.f1409n = strArr;
        this.f1410o = strArr2;
    }

    public n(Parcel parcel) {
        String[] strArr = f1395p;
        this.f1409n = strArr;
        this.f1410o = strArr;
        this.f1396a = parcel.readInt();
        this.f1397b = parcel.readInt();
        this.f1398c = parcel.readInt();
        this.f1399d = parcel.readInt();
        this.f1400e = parcel.readInt();
        this.f1401f = parcel.readInt() == 1;
        this.f1402g = parcel.readInt() == 1;
        this.f1403h = parcel.readInt() == 1;
        this.f1404i = parcel.readInt() == 1;
        this.f1405j = parcel.readInt() == 1;
        this.f1406k = parcel.readString();
        this.f1408m = parcel.readString();
        this.f1409n = parcel.createStringArray();
        this.f1410o = parcel.createStringArray();
    }

    public n(JSONObject jSONObject) {
        String[] strArr = f1395p;
        this.f1409n = strArr;
        this.f1410o = strArr;
        this.f1396a = jSONObject.optInt("space");
        this.f1397b = jSONObject.optInt("verCode");
        this.f1398c = jSONObject.optInt("flags");
        this.f1399d = jSONObject.optInt("appId");
        this.f1400e = jSONObject.optInt("installSdk");
        this.f1401f = jSONObject.optBoolean("installMode");
        this.f1402g = jSONObject.optBoolean("splitApk");
        this.f1403h = jSONObject.optBoolean("requestAssistant");
        this.f1404i = jSONObject.optBoolean("extractNativeLibs");
        this.f1405j = jSONObject.optBoolean("useSysAbi");
        this.f1406k = jSONObject.optString("pkg");
        this.f1408m = jSONObject.optString("baseCodePath");
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("splitSourcePath");
        if (jSONArrayOptJSONArray != null) {
            this.f1409n = new String[jSONArrayOptJSONArray.length()];
            for (int i4 = 0; i4 < jSONArrayOptJSONArray.length(); i4++) {
                this.f1409n[i4] = jSONArrayOptJSONArray.getString(i4);
            }
        }
        JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("supportedAbiList");
        if (jSONArrayOptJSONArray2 != null) {
            this.f1410o = new String[jSONArrayOptJSONArray2.length()];
            for (int i5 = 0; i5 < jSONArrayOptJSONArray2.length(); i5++) {
                this.f1410o[i5] = jSONArrayOptJSONArray2.optString(i5);
            }
        }
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
