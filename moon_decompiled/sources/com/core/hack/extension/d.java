package com.core.hack.extension;

import android.app.ActivityManager;
import android.content.Context;
import androidx.core.os.perationCompat;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final boolean f255a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f256b = "d";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final k<d> f257c;

    public class a extends k<d> {
        static {
            perationCompat.init0(548);
        }

        @Override // v3.k
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public native d a();
    }

    static {
        perationCompat.init0(392);
        f257c = new a();
    }

    private d() {
    }

    public /* synthetic */ d(a aVar) {
        this();
    }

    public static native d c();

    private native void e(Object... objArr);

    private native ActivityManager.AppTask f(Context context, int i4);

    private native void g(Object... objArr);

    private native void h(Object... objArr);

    public native Object d(int i4, Object... objArr);
}
