package com.core.hack.extension;

import android.os.IBinder;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f253a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IBinder f254b;

    static {
        perationCompat.init0(425);
    }

    public /* synthetic */ c(int i4, IBinder iBinder) {
        this.f253a = i4;
        this.f254b = iBinder;
    }

    @Override // java.lang.Runnable
    public final native void run();
}
