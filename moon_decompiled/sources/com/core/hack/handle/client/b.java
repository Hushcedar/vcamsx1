package com.core.hack.handle.client;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.io.Serializable;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public class b extends com.core.hack.handle.b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final String f270h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final Object f271i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public j f272j;

    static {
        perationCompat.init0(484);
    }

    public b(Serializable serializable, String str) {
        this.f270h = str;
        this.f271i = serializable;
    }

    @Override // com.core.hack.handle.b
    public final native String c();

    @Override // com.core.hack.handle.b
    public native void h(j[] jVarArr, j[] jVarArr2);

    @Override // com.core.hack.handle.b
    public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

    public native boolean m();
}
