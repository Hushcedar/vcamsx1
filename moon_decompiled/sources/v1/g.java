package v1;

import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public final class g<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Field f1716a;

    public g(Class cls, Field field) throws NoSuchFieldException {
        if (!field.isAnnotationPresent(b.class)) {
            throw new NoSuchFieldException(field.getName());
        }
        Field declaredField = cls.getDeclaredField(((b) field.getAnnotation(b.class)).name());
        this.f1716a = declaredField;
        declaredField.setAccessible(true);
    }

    public final T a() {
        try {
            return (T) this.f1716a.get(null);
        } catch (Exception unused) {
            return null;
        }
    }

    public final void b(T t4) {
        try {
            this.f1716a.set(null, t4);
        } catch (Exception unused) {
        }
    }
}
