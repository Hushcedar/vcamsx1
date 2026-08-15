package j3;

import android.accounts.Account;
import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import androidx.core.os.perationCompat;
import j3.e;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class h extends e.f {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final /* synthetic */ Account f635q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final /* synthetic */ String f636r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public final /* synthetic */ Bundle f637s;

    static {
        perationCompat.init0(156);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(e eVar, v vVar, e.h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, String str2, Account account, String str3, Bundle bundle) {
        super(vVar, hVar, iAccountManagerResponse, str, z3, true, str2, false, true);
        this.f635q = account;
        this.f636r = str3;
        this.f637s = bundle;
    }

    @Override // j3.e.f
    public final native void H3();

    @Override // j3.e.f
    public final native String I3(long j4);
}
