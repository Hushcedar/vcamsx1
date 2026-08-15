package a;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.IInterface;
import android.text.TextUtils;
import android.util.Log;
import k2.k;
import v1.g;
import v3.i;
import v3.j;
import x1.m;

/* JADX INFO: loaded from: classes.dex */
public final class b extends Application {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static b f0a;

    public b() {
        System.loadLibrary("multiapp");
    }

    public static b a() {
        b bVar;
        synchronized (b.class) {
            if (f0a == null) {
                f0a = new b();
            }
            bVar = f0a;
        }
        return bVar;
    }

    @Override // android.content.ContextWrapper
    public final void attachBaseContext(Context context) {
        g<Object> gVar;
        g<IInterface> gVar2;
        super.attachBaseContext(context);
        Log.i(n.b.f1119f, "start " + context.getPackageName());
        Object objInvoke = null;
        if (Build.VERSION.SDK_INT == 29) {
            boolean z3 = t1.c.f1560a;
            if (!TextUtils.isEmpty(k.f701a != null ? r2.a(new Object[]{"ro.vivo.os.version"}) : null)) {
                g<IInterface> gVar3 = e3.a.f329a;
                if ((gVar3 != null ? gVar3.a() : null) != null && (gVar2 = e3.a.f329a) != null) {
                    gVar2.b(null);
                }
            } else {
                g<Object> gVar4 = d3.a.f279a;
                if ((gVar4 != null ? gVar4.a() : null) != null && (gVar = d3.a.f279a) != null) {
                    gVar.b(null);
                }
            }
        }
        if (!p.b.f1190i) {
            synchronized (p.b.class) {
                boolean z4 = p.b.f1190i;
                if (!z4) {
                    if (!z4 && p.b.f1186e == 0) {
                        Object[] objArr = new Object[0];
                        j.a<Integer> aVar = p.b.f1191j;
                        aVar.getClass();
                        try {
                            objInvoke = aVar.f1735a.invoke(null, objArr);
                        } catch (Exception unused) {
                        }
                        Integer num = (Integer) objInvoke;
                        if (num != null) {
                            p.b.f1186e = num.intValue();
                        }
                    }
                    try {
                        context.getExternalCacheDir();
                    } catch (Exception unused2) {
                    }
                    p.b.a(context);
                    p.b.c(context);
                    p.b.b(context);
                    p.b.g(context);
                    p.b.f1185d = context.getPackageName();
                    p.b.f1190i = true;
                }
            }
        }
        n3.a.a().f(context);
        f.e.e().s(context);
        i.e(context);
        if (i.b()) {
            u1.c.a().h();
        }
    }

    @Override // android.app.Application
    public final void onCreate() {
        super.onCreate();
        if (!i.b()) {
            if (i.c()) {
                f.e.e().c(this);
                f.e.e().b(this);
                return;
            }
            return;
        }
        z.a aVarA = z.a.a();
        synchronized (aVarA) {
            if (!aVarA.f1898c) {
                v1.f<Instrumentation> fVar = x1.d.f1794l;
                if (aVarA.f1896a == null) {
                    aVarA.f1896a = x1.d.f1792j.a(null);
                }
                Instrumentation instrumentationA = fVar.a(aVarA.f1896a, null);
                aVarA.f1897b = instrumentationA;
                if (Build.VERSION.SDK_INT >= 28) {
                    v1.c<Object> cVar = m.f1836a;
                    cVar.b(aVarA, cVar.a(instrumentationA));
                }
                v1.c<Instrumentation> cVar2 = x1.d.f1786d;
                if (aVarA.f1896a == null) {
                    aVarA.f1896a = x1.d.f1792j.a(null);
                }
                cVar2.b(aVarA.f1896a, aVarA);
                aVarA.f1898c = true;
            }
        }
    }
}
