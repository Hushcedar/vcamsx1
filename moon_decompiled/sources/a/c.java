package a;

import android.os.Bundle;
import i.t;
import v3.j;

/* JADX INFO: loaded from: classes.dex */
public final class c extends t.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f1a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final j.a<Void> f2b;

    public c(Object obj) {
        this.f1a = obj;
        Class<?> cls = obj.getClass();
        Class cls2 = Integer.TYPE;
        this.f2b = new j.a<>(cls, "onPackageInstalled", new Class[]{String.class, cls2, String.class, Bundle.class, cls2});
    }

    @Override // i.t.a, i.t
    public final void B(String str, int i4, String str2, Bundle bundle, int i5) {
        Object[] objArr = {str, Integer.valueOf(i4), str2, bundle, Integer.valueOf(i5)};
        Object obj = this.f1a;
        j.a<Void> aVar = this.f2b;
        aVar.getClass();
        try {
            aVar.f1735a.invoke(obj, objArr);
        } catch (Exception unused) {
        }
    }
}
