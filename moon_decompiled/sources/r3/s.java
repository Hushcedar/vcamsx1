package r3;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import t3.a;

/* JADX INFO: loaded from: classes.dex */
public final class s implements Parcelable, v3.f {
    public static final Parcelable.Creator<s> CREATOR;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public static final u f1413v;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1414a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1415b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1416c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f1417d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final String f1418e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final String[] f1419f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final long[] f1420g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f1421h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final String f1422i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final long f1423j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final long f1424k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final long f1425l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public long f1426m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final HashSet f1427n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public String f1428o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final int f1429p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public int f1430q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final int f1431r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final SparseArray<u> f1432s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final n f1433t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public a.e f1434u;

    public class a implements Parcelable.Creator<s> {
        static {
            perationCompat.init0(383);
        }

        @Override // android.os.Parcelable.Creator
        public final native s createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native s[] newArray(int i4);
    }

    static {
        perationCompat.init0(716);
        f1413v = new u();
        CREATOR = new a();
    }

    public s(Parcel parcel) {
        this.f1427n = new HashSet();
        this.f1429p = -1;
        this.f1432s = new SparseArray<>();
        this.f1414a = parcel.readString();
        this.f1415b = parcel.readInt();
        this.f1417d = parcel.readString();
        this.f1416c = parcel.readString();
        this.f1418e = parcel.readString();
        this.f1419f = parcel.createStringArray();
        this.f1420g = parcel.createLongArray();
        this.f1421h = parcel.readString();
        this.f1422i = parcel.readString();
        this.f1423j = parcel.readLong();
        this.f1424k = parcel.readLong();
        this.f1425l = parcel.readLong();
        this.f1426m = parcel.readLong();
        int i4 = parcel.readInt();
        for (int i5 = 0; i5 < i4; i5++) {
            this.f1427n.add(parcel.readString());
        }
        this.f1428o = parcel.readString();
        this.f1429p = parcel.readInt();
        this.f1430q = parcel.readInt();
        this.f1431r = parcel.readInt();
        int i6 = parcel.readInt();
        for (int i7 = 0; i7 < i6; i7++) {
            this.f1432s.put(parcel.readInt(), new u(parcel));
        }
        this.f1433t = new n(parcel);
    }

    public s(String str, int i4, String str2, n nVar) {
        this.f1427n = new HashSet();
        this.f1429p = -1;
        this.f1432s = new SparseArray<>();
        this.f1414a = str;
        this.f1415b = i4;
        this.f1416c = str2;
        this.f1433t = nVar;
    }

    public s(String str, int i4, String str2, n nVar, SparseArray<u> sparseArray) {
        this.f1427n = new HashSet();
        this.f1429p = -1;
        this.f1432s = new SparseArray<>();
        this.f1414a = str;
        this.f1415b = i4;
        this.f1416c = str2;
        this.f1433t = nVar;
        this.f1432s = sparseArray;
    }

    public s(JSONObject jSONObject) throws JSONException {
        long[] jArr;
        this.f1427n = new HashSet();
        this.f1429p = -1;
        this.f1432s = new SparseArray<>();
        this.f1414a = jSONObject.optString("pkg");
        this.f1415b = jSONObject.optInt("appId");
        this.f1417d = jSONObject.optString("sharedUserId");
        this.f1416c = jSONObject.optString("codePath");
        this.f1418e = jSONObject.optString("resourcePath");
        this.f1419f = v3.e.d(jSONObject.optJSONArray("usesStaticLibraries"));
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("usesStaticLibrariesVersions");
        if (jSONArrayOptJSONArray == null) {
            jArr = null;
        } else {
            long[] jArr2 = new long[jSONArrayOptJSONArray.length()];
            for (int i4 = 0; i4 < jSONArrayOptJSONArray.length(); i4++) {
                jArr2[i4] = jSONArrayOptJSONArray.optLong(i4);
            }
            jArr = jArr2;
        }
        this.f1420g = jArr;
        this.f1421h = jSONObject.optString("primaryCpuAbi");
        this.f1422i = jSONObject.optString("secondaryCpuAbi");
        this.f1423j = jSONObject.optLong("timestamp");
        this.f1424k = jSONObject.optLong("firstInstallTime");
        this.f1425l = jSONObject.optLong("lastUpdateTime");
        this.f1426m = jSONObject.optLong("verCode");
        String[] strArrD = v3.e.d(jSONObject.optJSONArray("oldCodePaths"));
        if (strArrD != null) {
            this.f1427n.addAll(Arrays.asList(strArrD));
        }
        this.f1428o = jSONObject.optString("installer");
        this.f1429p = jSONObject.optInt("categoryHint");
        this.f1430q = jSONObject.optInt("pkgFlags");
        this.f1431r = jSONObject.optInt("pkgPrivateFlags");
        JSONArray jSONArray = jSONObject.getJSONArray("userState");
        for (int i5 = 0; i5 < jSONArray.length(); i5++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i5);
            this.f1432s.put(jSONObject2.optInt("space"), new u(jSONObject2.optJSONObject("data")));
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("installSource");
        if (jSONObjectOptJSONObject != null) {
            this.f1433t = new n(jSONObjectOptJSONObject);
        }
    }

    @Override // v3.f
    public final native void a(JSONObject jSONObject);

    public final native int b(int i4);

    public final native int[] c();

    public final native boolean d(int i4);

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native ArrayList e(int i4);

    public final native u f(int i4);

    public final native u g(int i4, boolean z3, boolean z4);

    public final native u h(int i4);

    public final native String toString();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
