package a;

import android.app.Activity;
import android.app.Application;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.text.TextUtils;
import b2.a;
import i.h;
import i.s;
import i.t;
import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Map;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final k<f> f5c = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Application f6a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public InvocationHandler f7b;

    public class a extends k<f> {
        @Override // v3.k
        public final f a() {
            return new f();
        }
    }

    public static f e() {
        return f5c.b();
    }

    public int A(Activity activity, Intent intent, int i4) {
        return i.d.n().Y(activity, intent, i4, new Intent());
    }

    public void B(InvocationHandler invocationHandler) {
        this.f7b = invocationHandler;
    }

    public Object C(Object obj) {
        d dVar = new d(obj);
        h.o().l0(dVar);
        return dVar;
    }

    public Object D(Object obj) {
        c cVar = new c(obj);
        h.o().m0(cVar);
        return cVar;
    }

    public void E(Object... objArr) {
        InvocationHandler invocationHandler = this.f7b;
        if (invocationHandler != null) {
            try {
                invocationHandler.invoke(invocationHandler, null, objArr);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public ResolveInfo F(Intent intent, String str, int i4, int i5) {
        return h.o().q0(intent, str, i4, i5);
    }

    public boolean G(int i4, String str) {
        return i.a.a().e(i4, str);
    }

    public int H(Intent intent, Bundle bundle, int i4) {
        return i.d.n().e0(intent, bundle, i4, new Intent());
    }

    public boolean I(String str, int i4, int i5) {
        return i.d.n().i0(str, i4, i5);
    }

    public int J(int i4, String str, int i5) {
        return h.o().x0(i4, str, i5);
    }

    public void K(Object obj) {
        h.o().y0((s) obj);
    }

    public void L(Object obj) {
        h.o().z0((t) obj);
    }

    public boolean M(String str, int i4, int i5) {
        return i.d.n().o0(str, i4, i5);
    }

    public boolean N(String str, int i4, int i5, boolean z3) {
        return h.o().A0(str, i4, i5, z3);
    }

    public void a(Application application, Context context) {
        this.f6a = application;
        b.a().attachBaseContext(context);
    }

    public int b() {
        c.a.sibt(true);
        try {
            int callingUid = Binder.getCallingUid();
            String strR = i.d.n().r(Binder.getCallingPid());
            if (!TextUtils.isEmpty(strR)) {
                if (com.core.hack.extension.a.E().p(strR)) {
                    callingUid = 90002;
                }
            }
            return callingUid;
        } finally {
            c.a.sibt(false);
        }
    }

    public int c(int i4, String str, int i5) {
        return h.o().c(i4, str, i5);
    }

    public int d(int i4, String str, int i5) {
        return h.o().d(i4, str, i5);
    }

    public ActivityInfo f(ComponentName componentName, int i4, int i5) {
        return h.o().p(componentName, i4, i5);
    }

    public int[] g() {
        return h.o().r();
    }

    public int[] h(String str) {
        return h.o().s(str);
    }

    public String i(int i4, String str) {
        o3.a aVarC = i.a.a().c(i4, str);
        if (aVarC != null) {
            return aVarC.toString();
        }
        return null;
    }

    public Application j() {
        return this.f6a;
    }

    public List k(int i4, int i5) {
        return h.o().y(i4, i5);
    }

    public Intent l(String str, int i4) {
        return h.o().A(str, i4);
    }

    public PackageInfo m(int i4, String str, int i5) {
        return h.o().D(i4, str, i5);
    }

    public Bundle n(String str, int i4, int i5) {
        return h.o().G(str, i4, i5);
    }

    public String[] o(int i4) {
        return i.d.n().x(i4);
    }

    public Map p(int i4) {
        return i.d.n().z(i4);
    }

    public int q() {
        return i.d.n().A();
    }

    public Map r(int i4, int i5) {
        return h.o().S(i4, i5);
    }

    public Object s(Object[] objArr) {
        int iIntValue = ((Integer) objArr[0]).intValue();
        if (iIntValue == 1) {
            return Boolean.valueOf(c.a.sSb());
        }
        if (iIntValue != 2) {
            return null;
        }
        return Long.valueOf(c.a.gec32());
    }

    public boolean t(int i4, String str) {
        return i.d.n().I(str, i4);
    }

    public int u(int i4, String str, int i5, ParcelFileDescriptor[] parcelFileDescriptorArr) {
        return h.o().V(i4, str, i5, parcelFileDescriptorArr);
    }

    public boolean v(int i4, String str) {
        return h.o().a0(str, i4);
    }

    public boolean w(int i4, String str, String str2) {
        i.d.n().O(str, i4, str2);
        return true;
    }

    public Parcel x() {
        AttributionSource attributionSourceBuild;
        if (Build.VERSION.SDK_INT < 31 || (attributionSourceBuild = new AttributionSource.Builder(Process.myUid()).setPackageName(f.e.e().n()).build()) == null) {
            return null;
        }
        return a.C0005a.a(b2.a.b(attributionSourceBuild));
    }

    public void y() {
        b.a().onCreate();
    }

    public List z(Intent intent, String str, int i4, int i5) {
        return h.o().g0(intent, str, i4, i5);
    }
}
