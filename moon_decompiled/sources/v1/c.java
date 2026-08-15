package v1;

import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class c<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Field f1714a;

    public c(Class cls, Field field) throws NoSuchFieldException {
        if (!field.isAnnotationPresent(b.class)) {
            throw new NoSuchFieldException(field.getName());
        }
        Field declaredField = cls.getDeclaredField(((b) field.getAnnotation(b.class)).name());
        this.f1714a = declaredField;
        declaredField.setAccessible(true);
    }

    public final T a(Object obj) {
        try {
            return (T) this.f1714a.get(obj);
        } catch (Exception unused) {
            return null;
        }
    }

    public final void b(Object obj, T t4) {
        try {
            this.f1714a.set(obj, t4);
        } catch (Exception unused) {
        }
    }
}
