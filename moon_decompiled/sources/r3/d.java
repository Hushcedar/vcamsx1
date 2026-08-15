package r3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import androidx.core.os.perationCompat;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import t3.a;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final Set<String> f1311i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final r3.c f1312j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static i f1313k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static h f1314l;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f1315a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final a f1316b = new a();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final b f1317c = new b();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final a f1318d = new a();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final c f1319e = new c();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ArrayMap<String, a.h> f1320f = new ArrayMap<>();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final boolean f1321g = true;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public ArrayList f1322h;

    public static final class a extends i3.h<a.b, ResolveInfo> {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final ArrayMap<ComponentName, a.C0088a> f1323i = new ArrayMap<>();

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public int f1324j;

        static {
            perationCompat.init0(518);
        }

        public static native void q(a aVar, a.C0088a c0088a, String str, ArrayList arrayList);

        public static native void r(a aVar, a.C0088a c0088a);

        @Override // i3.h
        public final native boolean c(IntentFilter intentFilter, ArrayList arrayList);

        @Override // i3.h
        public final /* bridge */ /* synthetic */ void e(PrintWriter printWriter, IntentFilter intentFilter) {
        }

        @Override // i3.h
        public final native boolean g(IntentFilter intentFilter, String str);

        @Override // i3.h
        public final native IntentFilter[] h(int i4);

        @Override // i3.h
        public final native ResolveInfo i(IntentFilter intentFilter, int i4, int i5);

        @Override // i3.h
        public final native List<ResolveInfo> j(Intent intent, String str, boolean z3, int i4);

        @Override // i3.h
        public final native void o(ArrayList arrayList);

        public final native ArrayList s(Intent intent, String str, int i4, int i5);

        public final native ArrayList t(Intent intent, String str, int i4, ArrayList arrayList, int i5);
    }

    public static final class b extends i3.h<a.i, ResolveInfo> {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final ArrayMap<ComponentName, a.h> f1325i = new ArrayMap<>();

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public int f1326j;

        static {
            perationCompat.init0(517);
        }

        @Override // i3.h
        public final native boolean c(IntentFilter intentFilter, ArrayList arrayList);

        @Override // i3.h
        public final /* bridge */ /* synthetic */ void e(PrintWriter printWriter, IntentFilter intentFilter) {
        }

        @Override // i3.h
        public final native boolean g(IntentFilter intentFilter, String str);

        @Override // i3.h
        public final native IntentFilter[] h(int i4);

        @Override // i3.h
        public final native ResolveInfo i(IntentFilter intentFilter, int i4, int i5);

        @Override // i3.h
        public final native List<ResolveInfo> j(Intent intent, String str, boolean z3, int i4);

        @Override // i3.h
        public final native void o(ArrayList arrayList);

        public final native ArrayList q(Intent intent, String str, int i4, int i5);

        public final native ArrayList r(Intent intent, String str, int i4, ArrayList arrayList, int i5);
    }

    public static final class c extends i3.h<a.k, ResolveInfo> {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final ArrayMap<ComponentName, a.j> f1327i = new ArrayMap<>();

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public int f1328j;

        static {
            perationCompat.init0(515);
        }

        @Override // i3.h
        public final native boolean c(IntentFilter intentFilter, ArrayList arrayList);

        @Override // i3.h
        public final /* bridge */ /* synthetic */ void e(PrintWriter printWriter, IntentFilter intentFilter) {
        }

        @Override // i3.h
        public final native boolean g(IntentFilter intentFilter, String str);

        @Override // i3.h
        public final native IntentFilter[] h(int i4);

        @Override // i3.h
        public final native ResolveInfo i(IntentFilter intentFilter, int i4, int i5);

        @Override // i3.h
        public final native List<ResolveInfo> j(Intent intent, String str, boolean z3, int i4);

        @Override // i3.h
        public final native void o(ArrayList arrayList);

        public final native ArrayList q(Intent intent, String str, int i4, int i5);

        public final native ArrayList r(Intent intent, String str, int i4, ArrayList arrayList, int i5);
    }

    static {
        perationCompat.init0(736);
        ArraySet arraySet = new ArraySet();
        f1311i = arraySet;
        arraySet.add("android.intent.action.SEND");
        arraySet.add("android.intent.action.SENDTO");
        arraySet.add("android.intent.action.SEND_MULTIPLE");
        arraySet.add("android.intent.action.VIEW");
        f1312j = new r3.c(0);
    }

    public d(i iVar, h hVar, HashMap map) {
        f1314l = hVar;
        f1313k = iVar;
        this.f1315a = map;
    }

    public final native void a(a.e eVar);

    public final native void b(a.e eVar);

    public final native void c(a.e eVar);

    public final native a.C0088a d(ComponentName componentName);

    public final native a.h e(ComponentName componentName);

    public final native a.C0088a f(ComponentName componentName);

    public final native a.j g(ComponentName componentName);

    public final native ArrayList h(Intent intent, String str, int i4, int i5);

    public final native ArrayList i(Intent intent, String str, int i4, ArrayList arrayList, int i5);

    public final native ProviderInfo j(int i4, String str, int i5);

    public final native ArrayList k(Intent intent, String str, int i4, int i5);

    public final native ArrayList l(Intent intent, String str, int i4, ArrayList arrayList, int i5);

    public final native ArrayList m(String str, int i4, int i5, int i6);

    public final native ArrayList n(Intent intent, String str, int i4, int i5);

    public final native ArrayList o(Intent intent, String str, int i4, ArrayList arrayList, int i5);

    public final native ArrayList p(Intent intent, String str, int i4, int i5);

    public final native ArrayList q(Intent intent, String str, int i4, ArrayList arrayList, int i5);

    public final native void r(a.e eVar);

    public final native void s(a.e eVar);
}
