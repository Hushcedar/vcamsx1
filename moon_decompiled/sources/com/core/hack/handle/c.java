package com.core.hack.handle;

import android.os.Parcel;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final boolean f265d = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f266a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f267b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected SparseArray<b> f268c;

    static {
        perationCompat.init0(694);
    }

    public c(String str) {
        this.f267b = str;
    }

    private native synchronized void e();

    public native String a(String str);

    public native b b(Method method);

    public abstract HashMap<String, b> c();

    public native void d();

    public native boolean f(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
}
