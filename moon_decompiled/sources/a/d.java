package a;

import i.s;
import v3.j;

/* JADX INFO: loaded from: classes.dex */
public final class d extends s.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f3a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final j.a<Void> f4b;

    public d(Object obj) {
        this.f3a = obj;
        Class<?> cls = obj.getClass();
        Class cls2 = Integer.TYPE;
        this.f4b = new j.a<>(cls, "onPackageDeleted", new Class[]{String.class, cls2, String.class, cls2});
    }

    @Override // i.s.a, i.s
    public final void o0(String str, int i4, String str2, int i5) {
        Object[] objArr = {str, Integer.valueOf(i4), str2, Integer.valueOf(i5)};
        Object obj = this.f3a;
        j.a<Void> aVar = this.f4b;
        aVar.getClass();
        try {
            aVar.f1735a.invoke(obj, objArr);
        } catch (Exception unused) {
        }
    }
}
