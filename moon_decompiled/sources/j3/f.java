package j3;

import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import androidx.core.os.perationCompat;
import j3.e;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class f extends e.f {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final /* synthetic */ Bundle f623q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final /* synthetic */ String f624r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final /* synthetic */ String[] f625s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final /* synthetic */ String f626t;

    static {
        perationCompat.init0(154);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(e eVar, v vVar, e.h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, Bundle bundle, String str2, String[] strArr, String str3) {
        super(vVar, hVar, iAccountManagerResponse, str, z3, true, null, false, true);
        this.f623q = bundle;
        this.f624r = str2;
        this.f625s = strArr;
        this.f626t = str3;
    }

    @Override // j3.e.f
    public final native void H3();

    @Override // j3.e.f
    public final native String I3(long j4);
}
