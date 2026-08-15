package b2;

import android.content.AttributionSource;
import android.os.Parcel;
import android.os.Parcelable;
import v1.i;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Class f59a = j.e.q(a.class, "android.content.AttributionSource");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.d(name = "", value = {@i(strings = {"android.content.AttributionSourceState"}, type = 1)})
    public static v1.a<Object> f60b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @v1.b(name = "CREATOR")
    private static v1.g<Parcelable.Creator<Parcelable>> f61c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @v1.b(name = "mAttributionSourceState")
    private static v1.c<Object> f62d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @v1.e(name = "asScopedParcelState", value = {})
    private static v1.f<Object> f63e;

    /* JADX INFO: renamed from: b2.a$a, reason: collision with other inner class name */
    public static class C0005a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @v1.e(name = "getParcel", value = {})
        private static v1.f<Parcel> f64a;

        static {
            j.e.q(C0005a.class, "android.content.AttributionSource$ScopedParcelState");
        }

        public static Parcel a(Object obj) {
            v1.f<Parcel> fVar = f64a;
            if (fVar == null || obj == null) {
                return null;
            }
            return fVar.a(obj, j.e.f514r);
        }
    }

    public static Parcelable.Creator<Parcelable> a() {
        v1.g<Parcelable.Creator<Parcelable>> gVar = f61c;
        if (gVar != null) {
            return gVar.a();
        }
        return null;
    }

    public static Object b(AttributionSource attributionSource) {
        v1.f<Object> fVar = f63e;
        if (fVar != null) {
            return fVar.a(attributionSource, j.e.f514r);
        }
        return null;
    }

    public static boolean c(Class cls) {
        Class cls2 = f59a;
        return cls2 != null && cls == cls2;
    }

    public static Object d(Object obj) {
        v1.c<Object> cVar = f62d;
        if (cVar != null) {
            return cVar.a(obj);
        }
        return null;
    }
}
