package j3;

import android.accounts.Account;
import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import androidx.core.os.perationCompat;
import j3.e;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class g extends e.f {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final /* synthetic */ Bundle f627q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final /* synthetic */ Account f628r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final /* synthetic */ String f629s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public final /* synthetic */ boolean f630t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public final /* synthetic */ boolean f631u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public final /* synthetic */ String f632v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public final /* synthetic */ byte[] f633w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public final /* synthetic */ e f634x;

    static {
        perationCompat.init0(155);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(e eVar, v vVar, e.h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, String str2, Bundle bundle, Account account, String str3, boolean z4, boolean z5, String str4, byte[] bArr) {
        super(eVar, vVar, hVar, iAccountManagerResponse, str, z3, false, str2);
        this.f634x = eVar;
        this.f627q = bundle;
        this.f628r = account;
        this.f629s = str3;
        this.f630t = z4;
        this.f631u = z5;
        this.f632v = str4;
        this.f633w = bArr;
    }

    @Override // j3.e.f
    public final native void H3();

    @Override // j3.e.f
    public final native String I3(long j4);

    @Override // j3.e.f
    public final native void onResult(Bundle bundle);
}
