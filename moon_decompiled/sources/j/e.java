package j;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Pair;
import androidx.core.os.perationCompat;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f497a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final u1.g f498b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final u1.i f499c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final u1.g f500d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final u1.g f501e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final u1.g f502f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final u1.g f503g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final u1.g f504h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final u1.g f505i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final u1.h f506j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final u1.l f507k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final u1.g f508l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final u1.g f509m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final u1.g f510n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final u1.g f511o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static final u1.g f512p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public static final int[] f513q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public static final Object[] f514r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public static final String[] f515s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public static final String[] f516t;

    public static native int A(XmlPullParser xmlPullParser, String str, int i4);

    public static native long B(XmlPullParser xmlPullParser, String str);

    public static native void C(int i4, int i5);

    public static native void D(FileDescriptor fileDescriptor, byte[] bArr, int i4);

    public static native void E(j3.d dVar, String str, int i4);

    public static native void F(j3.d dVar, String str, CharSequence charSequence);

    public static native void a(List list, List list2, c0.g gVar, int i4);

    public static native void b(String str, boolean z3);

    public static native void c(Closeable closeable);

    public static native boolean d(Object obj, Object[] objArr);

    public static native ArrayList e(IBinder iBinder);

    public static native int f(Object[] objArr, Class cls, int i4);

    public static native boolean g(Class[] clsArr, Object[] objArr);

    public static native void h(Class cls, HashSet hashSet);

    public static native int i();

    public static native int j();

    public static native ApplicationInfo k(ResolveInfo resolveInfo);

    public static native ComponentName l(int i4, Object obj);

    public static native String m(Context context);

    public static native Pair n(Field field);

    public static native void o(Exception exc);

    public static native int p(Object[] objArr, Object obj, int i4);

    public static native Class q(Class cls, String str);

    public static native void r(Class cls, Class cls2);

    public static native boolean s(Collection collection);

    public static native boolean t(Object[] objArr);

    public static native String u(List list);

    public static native int v(Object[] objArr, Class cls);

    public static native int w();

    public static native int x(FileDescriptor fileDescriptor, byte[] bArr, int i4);

    public static native boolean y(XmlPullParser xmlPullParser, String str, boolean z3);

    public static native int z(XmlPullParser xmlPullParser, String str);

    static {
        perationCompat.init0(2);
        f498b = new u1.g(1);
        f499c = new u1.i();
        f500d = new u1.g(12);
        f501e = new u1.g(16);
        f502f = new u1.g(9);
        f503g = new u1.g(6);
        f504h = new u1.g(20);
        f505i = new u1.g(22);
        f506j = new u1.h();
        f507k = new u1.l();
        f508l = new u1.g(4);
        f509m = new u1.g(13);
        f510n = new u1.g(17);
        f511o = new u1.g(21);
        f512p = new u1.g(15);
        f513q = new int[0];
        f514r = new Object[0];
        f515s = new String[0];
        f516t = new String[0];
    }
}
