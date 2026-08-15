package com.core.hack.extension;

import android.content.AttributionSource;
import android.content.AttributionSourceState;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class a implements l.b {
    private static final k<a> A;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final boolean f228s = false;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private static final String f229t = "moon";

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public static final int f230u = -1;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public static final int f231v = 0;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public static final int f232w = 1;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public static final int f233x = 2;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public static final int f234y = 3;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public static final int f235z = 1;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f236g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final HashSet<String> f237h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final HashSet<String> f238i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final HashMap<String, HashSet<String>> f239j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final HashSet<String> f240k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final HashSet<String> f241l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final HashSet<String> f242m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final HashSet<String> f243n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final HashSet<String> f244o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final Set<String> f245p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final HashSet<String> f246q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final HashSet<String> f247r;

    /* JADX INFO: renamed from: com.core.hack.extension.a$a, reason: collision with other inner class name */
    public class C0010a extends k<a> {
        static {
            perationCompat.init0(370);
        }

        @Override // v3.k
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public native a a();
    }

    public class b implements Parcelable.Creator<AttributionSource> {
        static {
            perationCompat.init0(371);
        }

        public b() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public native AttributionSource createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public native AttributionSource[] newArray(int i4);
    }

    public class c implements Parcelable.Creator<AttributionSourceState> {
        static {
            perationCompat.init0(368);
        }

        public c() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public native AttributionSourceState createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public native AttributionSourceState[] newArray(int i4);
    }

    static {
        perationCompat.init0(418);
        A = new C0010a();
    }

    private a() {
        this.f236g = -1;
        this.f237h = new HashSet<>();
        this.f238i = new HashSet<>();
        this.f239j = new HashMap<>();
        this.f240k = new HashSet<>();
        this.f241l = new HashSet<>();
        this.f242m = new HashSet<>();
        this.f243n = new HashSet<>();
        this.f244o = new HashSet<>();
        this.f245p = new HashSet();
        this.f246q = new HashSet<>();
        this.f247r = new HashSet<>();
        F();
    }

    public /* synthetic */ a(C0010a c0010a) {
        this();
    }

    private native void A(Object obj);

    private native void B(ApplicationInfo applicationInfo);

    private native void C(ApplicationInfo applicationInfo);

    private native void D(ApplicationInfo applicationInfo);

    public static native l.b E();

    private native void F();

    private static native boolean G(ApplicationInfo applicationInfo);

    private static native boolean H(ApplicationInfo applicationInfo);

    private static native boolean I(ApplicationInfo applicationInfo);

    private static native boolean J(ApplicationInfo applicationInfo);

    private static native boolean K(ApplicationInfo applicationInfo);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void L(Context context);

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void M() {
        Process.killProcess(Process.myPid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void N(ApplicationInfo applicationInfo, Context context);

    private native boolean O(String str);

    private native boolean P(String str);

    private native void Q();

    private native void R(String str);

    private native void S();

    private native void T();

    /* JADX INFO: Access modifiers changed from: private */
    public native void U(AttributionSourceState attributionSourceState, int i4);

    private native boolean V(String str);

    private native boolean W(String str);

    public static native a x();

    private native void z(Context context);

    public native boolean X(int i4);

    @Override // l.b
    public native boolean a(String str);

    @Override // l.b
    public native boolean b(String str);

    @Override // l.b
    public native void c(Context context, Context context2, ApplicationInfo applicationInfo);

    @Override // l.b
    public native boolean d(String str, String str2);

    @Override // l.b
    public native boolean e(boolean z3, boolean z4, String str);

    @Override // l.b
    public native boolean f(String str);

    @Override // l.b
    public native void g(String str);

    @Override // l.b
    public native void h(Context context, Context context2, ApplicationInfo applicationInfo);

    @Override // l.b
    public native Object i(int i4, Object... objArr);

    @Override // l.b
    public native void j(Context context, Context context2, ApplicationInfo applicationInfo);

    @Override // l.b
    public native int k(String str);

    @Override // l.b
    public native void l(Context context, ApplicationInfo applicationInfo);

    @Override // l.b
    public native void m(ApplicationInfo applicationInfo);

    @Override // l.b
    public native void n(Context context, ApplicationInfo applicationInfo);

    @Override // l.b
    public native int o(String str);

    @Override // l.b
    public native boolean p(String str);

    @Override // l.b
    public native void q(Context context, ApplicationInfo applicationInfo);

    @Override // l.b
    public native boolean r(ApplicationInfo applicationInfo);

    @Override // l.b
    public native boolean s(String str);

    @Override // l.b
    public native boolean t(ApplicationInfo applicationInfo);
}
