package v3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public final class j {
    public static Field a(Class cls, String str) {
        if (cls == null) {
            return null;
        }
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            return null;
        }
    }

    public static class a<T> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Method f1735a;

        public a(Class cls, String str, Class[] clsArr) {
            Method declaredMethod;
            try {
                declaredMethod = cls.getDeclaredMethod(str, clsArr);
            } catch (NoSuchMethodException unused) {
                declaredMethod = null;
            }
            this.f1735a = declaredMethod;
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
            }
        }

        public a(Class[] clsArr) {
            Method declaredMethod;
            try {
                try {
                    declaredMethod = Class.forName("android.os.UserHandle").getDeclaredMethod("myUserId", clsArr);
                } catch (NoSuchMethodException unused) {
                    declaredMethod = null;
                }
                this.f1735a = declaredMethod;
            } catch (Exception unused2) {
            }
            Method method = this.f1735a;
            if (method != null) {
                method.setAccessible(true);
            }
        }
    }
}
