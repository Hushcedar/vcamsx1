package v1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class a<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Constructor<T> f1713a;

    public a(Class cls, Field field) throws NoSuchMethodException {
        Constructor<T> declaredConstructor = cls.getDeclaredConstructor((Class[]) j.e.n(field).second);
        this.f1713a = declaredConstructor;
        declaredConstructor.setAccessible(true);
    }

    public final T a(Object[] objArr) {
        try {
            return this.f1713a.newInstance(objArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
