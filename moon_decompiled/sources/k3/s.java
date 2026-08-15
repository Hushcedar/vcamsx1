package k3;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import androidx.core.os.perationCompat;
import java.lang.ref.WeakReference;
import t1.i;

/* JADX INFO: loaded from: classes.dex */
public final class s extends i.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final p f899b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final a f900c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f901d;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public WeakReference<PendingIntent> f905h;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public String f907j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public String f908k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public String f909l;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f903f = false;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public boolean f904g = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final int f906i = Process.myUid();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final WeakReference<s> f902e = new WeakReference<>(this);

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f910a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f911b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final b f912c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final String f913d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final int f914e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final Intent f915f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public final String f916g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Bundle f917h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public Intent[] f918i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public String[] f919j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public final int f920k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        public final int f921l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public final int f922m;

        static {
            perationCompat.init0(513);
        }

        public a(int i4, String str, b bVar, String str2, int i5, Intent[] intentArr, String[] strArr, int i6, Bundle bundle, int i7) {
            this.f910a = i4;
            this.f911b = str;
            this.f912c = bVar;
            this.f913d = str2;
            this.f914e = i5;
            Intent intent = intentArr != null ? intentArr[intentArr.length - 1] : null;
            this.f915f = intent;
            String str3 = strArr != null ? strArr[strArr.length - 1] : null;
            this.f916g = str3;
            this.f918i = intentArr;
            this.f919j = strArr;
            this.f920k = i6;
            this.f917h = bundle;
            this.f922m = i7;
            int iHashCode = ((((851 + i6) * 37) + i5) * 37) + i7;
            iHashCode = str2 != null ? (iHashCode * 37) + str2.hashCode() : iHashCode;
            iHashCode = bVar != null ? (iHashCode * 37) + bVar.hashCode() : iHashCode;
            iHashCode = intent != null ? (iHashCode * 37) + intent.filterHashCode() : iHashCode;
            this.f921l = ((str.hashCode() + ((str3 != null ? (iHashCode * 37) + str3.hashCode() : iHashCode) * 37)) * 37) + i4;
        }

        public final native String a();

        public final native boolean equals(Object obj);

        public final native int hashCode();

        public final native String toString();
    }

    static {
        perationCompat.init0(299);
    }

    public s(p pVar, a aVar, int i4) {
        this.f899b = pVar;
        this.f900c = aVar;
        this.f901d = i4;
    }

    @Override // t1.i
    public final native void f1(Intent intent);

    public final native void finalize();

    @Override // t1.i
    public final native int h(int i4, Intent intent, String str, t1.h hVar, String str2);

    @Override // t1.i
    public final native PendingIntent h0();

    @Override // t1.i
    public final native int l(int i4, Intent intent, String str, t1.h hVar, String str2, IBinder iBinder, String str3, int i5, int i6, int i7, Bundle bundle);

    public final native String toString();
}
