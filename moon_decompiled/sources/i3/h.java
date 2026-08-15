package i3;

import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArrayMap;
import android.util.ArraySet;
import androidx.core.os.perationCompat;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import t3.a;

/* JADX INFO: loaded from: classes.dex */
public abstract class h<F extends IntentFilter, R> {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f464h;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ArraySet<F> f465a = new ArraySet<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ArrayMap<String, F[]> f466b = new ArrayMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final ArrayMap<String, F[]> f467c = new ArrayMap<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ArrayMap<String, F[]> f468d = new ArrayMap<>();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final ArrayMap<String, F[]> f469e = new ArrayMap<>();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ArrayMap<String, F[]> f470f = new ArrayMap<>();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final ArrayMap<String, F[]> f471g = new ArrayMap<>();

    public class a implements Comparator {
        static {
            perationCompat.init0(542);
        }

        @Override // java.util.Comparator
        public final native int compare(Object obj, Object obj2);
    }

    static {
        perationCompat.init0(130);
        f464h = new a();
    }

    public static native boolean f(IntentFilter intentFilter, IntentFilter intentFilter2);

    public final native void a(F f4);

    public final native void b(ArrayMap<String, F[]> arrayMap, String str, F f4);

    public abstract boolean c(IntentFilter intentFilter, ArrayList arrayList);

    public final native void d(Intent intent, v3.b bVar, boolean z3, boolean z4, String str, String str2, IntentFilter[] intentFilterArr, ArrayList arrayList, int i4);

    public native void e(PrintWriter printWriter, IntentFilter intentFilter);

    public abstract boolean g(IntentFilter intentFilter, String str);

    public abstract F[] h(int i4);

    public abstract R i(F f4, int i4, int i5);

    public native List<R> j(Intent intent, String str, boolean z3, int i4);

    public final native ArrayList k(Intent intent, String str, boolean z3, ArrayList arrayList, int i4);

    public final native int l(IntentFilter intentFilter, Iterator it, ArrayMap arrayMap);

    public final native void m(a.d dVar);

    public final native void n(ArrayMap arrayMap, String str, a.d dVar);

    public native void o(ArrayList arrayList);

    public final native int p(a.d dVar, Iterator it, ArrayMap arrayMap);
}
