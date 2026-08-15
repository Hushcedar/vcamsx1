package r3;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class e implements Parcelable {
    public static final Parcelable.Creator<e> CREATOR;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Object f1329a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f1330b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1331c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final boolean f1332d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final boolean f1333e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f1334f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f1335g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public Context f1336h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public PackageManager f1337i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public String f1338j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public boolean f1339k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public boolean f1340l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public ApplicationInfo f1341m;

    public class a implements Parcelable.Creator<e> {
        static {
            perationCompat.init0(700);
        }

        @Override // android.os.Parcelable.Creator
        public final native e createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native e[] newArray(int i4);
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1342a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f1343b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final boolean f1344c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final boolean f1345d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final boolean f1346e;

        static {
            perationCompat.init0(702);
        }

        public b(String str, String str2, boolean z3, boolean z4, boolean z5) {
            this.f1342a = str;
            this.f1343b = str2;
            this.f1344c = z3;
            this.f1345d = z4;
            this.f1346e = z5;
        }

        public final native String toString();
    }

    static {
        perationCompat.init0(738);
        CREATOR = new a();
    }

    public e(Parcel parcel) {
        this.f1330b = parcel.readString();
        this.f1331c = parcel.readString();
        this.f1332d = parcel.readByte() != 0;
        this.f1333e = parcel.readByte() != 0;
        this.f1334f = parcel.readByte() != 0;
        this.f1335g = parcel.readByte() != 0;
        this.f1338j = parcel.readString();
    }

    public e(String str, String str2, boolean z3, boolean z4, boolean z5, boolean z6, Context context) {
        this.f1330b = str;
        this.f1331c = str2;
        this.f1332d = z3;
        this.f1333e = z4;
        this.f1338j = null;
        d(context, z5, z6);
    }

    public static native String b(Object obj, String[] strArr);

    public final native int a(String str, boolean z3);

    public final native String c(boolean z3);

    public final native void d(Context context, boolean z3, boolean z4);

    @Override // android.os.Parcelable
    public final native int describeContents();

    public final native b e();

    public final native boolean f(String str);

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
