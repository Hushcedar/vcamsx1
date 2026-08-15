package c2;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f212a = j.e.q(k.class, "android.content.pm.ParceledListSlice");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static v1.g<Parcelable.Creator<?>> f213b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.e(name = "", value = {List.class})
    private static v1.a<Object> f214c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.e(name = "writeToParcel", value = {Parcel.class, int.class})
    private static v1.f<Void> f215d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.e(name = "getList", value = {})
    private static v1.f<List> f216e;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "writeToParcel", value = {Parcel.class, int.class})
        private static v1.f<Void> f217a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.e(name = "getList", value = {})
        private static v1.f<List> f218b;

        static {
            j.e.q(a.class, "android.content.pm.BaseParceledListSlice");
        }
    }

    public static Parcelable.Creator<?> a() {
        v1.g<Parcelable.Creator<?>> gVar = f213b;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }

    public static Object b(Class cls) {
        return (f214c == null || !d(cls)) ? Collections.emptyList() : f214c.a(new Object[]{Collections.emptyList()});
    }

    public static List c(Object obj) {
        v1.f<List> fVar;
        v1.f fVar2 = a.f218b;
        Object[] objArr = j.e.f514r;
        if (fVar2 != null) {
            fVar = a.f218b;
        } else {
            fVar = f216e;
            if (fVar == null) {
                return null;
            }
        }
        return fVar.a(obj, objArr);
    }

    public static boolean d(Class cls) {
        Class cls2 = f212a;
        return cls2 != null && cls == cls2;
    }

    public static Object e(List list) {
        v1.a<Object> aVar = f214c;
        if (aVar == null) {
            return null;
        }
        try {
            return aVar.a(new Object[]{list});
        } catch (Exception unused) {
            return null;
        }
    }

    public static Object f(List list, Class cls) {
        return (f214c == null || !d(cls)) ? list : f214c.a(new Object[]{list});
    }

    public static void g(Parcel parcel, Object obj) {
        v1.f<Void> fVar = f215d;
        if (fVar != null) {
            fVar.a(obj, new Object[]{parcel, 1});
        } else if (a.f217a != null) {
            a.f217a.a(obj, new Object[]{parcel, 1});
        }
    }
}
