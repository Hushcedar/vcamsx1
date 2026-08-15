package g;

import androidx.core.os.perationCompat;
import j.e;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f376a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Object f377b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final c f378c;

    public class a implements InvocationHandler {
        static {
            perationCompat.init0(701);
        }

        public a() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public final native Object invoke(Object obj, Method method, Object[] objArr);
    }

    public d(Object obj, c cVar) {
        this.f376a = obj;
        this.f378c = cVar;
        ClassLoader classLoader = obj.getClass().getClassLoader();
        Class<?> cls = obj.getClass();
        HashSet hashSet = new HashSet();
        e.h(cls, hashSet);
        Class[] clsArr = new Class[hashSet.size()];
        hashSet.toArray(clsArr);
        this.f377b = Proxy.newProxyInstance(classLoader, clsArr, new a());
        h.a aVar = (h.a) cVar;
        aVar.getClass();
    }
}
