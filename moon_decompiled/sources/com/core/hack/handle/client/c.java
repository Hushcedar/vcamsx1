package com.core.hack.handle.client;

import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class c extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final boolean f273l = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    protected boolean f274h = true;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected boolean f275i = true;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    protected int f276j = -1;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final String f277k;

    static {
        perationCompat.init0(482);
    }

    public c(String str) {
        this.f277k = str;
    }

    private native void m(Object obj, String str);

    @Override // com.core.hack.handle.b
    public native String c();

    @Override // com.core.hack.handle.b
    public native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

    public native boolean n(Class[] clsArr, Object[] objArr);

    public native boolean o(Object[] objArr);

    public native boolean p();

    public native boolean q();

    public native boolean r(Object[] objArr);

    public native boolean s();
}
