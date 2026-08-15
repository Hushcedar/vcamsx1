package v1;

import android.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class h<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Method f1717a;

    public h(Class cls, Field field) throws NoSuchMethodException {
        Pair pairN = j.e.n(field);
        Method declaredMethod = cls.getDeclaredMethod((String) pairN.first, (Class[]) pairN.second);
        this.f1717a = declaredMethod;
        declaredMethod.setAccessible(true);
    }

    public final T a(Object[] objArr) {
        try {
            return (T) this.f1717a.invoke(null, objArr);
        } catch (Exception unused) {
            return null;
        }
    }

    public final T b(Object[] objArr) {
        return (T) this.f1717a.invoke(null, objArr);
    }
}
