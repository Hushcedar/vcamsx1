package com.core.hack.handle;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final boolean f258g = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected Method f259a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected int f260b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected Class[] f261c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected Class f262d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private j[] f263e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private j[] f264f;

    static {
        perationCompat.init0(693);
    }

    public native Object[] a();

    public native Object[] b(Parcel parcel);

    public abstract String c();

    public native Method d();

    public native Class[] e();

    public native Class f();

    public native int g();

    public abstract void h(j[] jVarArr, j[] jVarArr2);

    public native void i(Parcel parcel, Object[] objArr);

    public native void j(Method method, int i4);

    public native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);

    public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
