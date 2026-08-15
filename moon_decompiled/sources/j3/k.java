package j3;

import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import androidx.core.os.perationCompat;
import j3.e;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class k extends e.f {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final /* synthetic */ String f641q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final /* synthetic */ String f642r;

    static {
        perationCompat.init0(160);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(e eVar, v vVar, e.h hVar, IAccountManagerResponse iAccountManagerResponse, String str, String str2, String str3) {
        super(eVar, vVar, hVar, iAccountManagerResponse, str, false, false, null);
        this.f641q = str2;
        this.f642r = str3;
    }

    @Override // j3.e.f
    public final native void H3();

    @Override // j3.e.f
    public final native String I3(long j4);

    @Override // j3.e.f
    public final native void onResult(Bundle bundle);
}
