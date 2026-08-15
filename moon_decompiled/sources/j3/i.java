package j3;

import android.accounts.IAccountManagerResponse;
import androidx.core.os.perationCompat;
import j3.e;
import k3.v;

/* JADX INFO: loaded from: classes.dex */
public final class i extends e.f {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final /* synthetic */ String f638q;

    static {
        perationCompat.init0(158);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(e eVar, v vVar, e.h hVar, IAccountManagerResponse iAccountManagerResponse, String str, boolean z3, String str2) {
        super(eVar, vVar, hVar, iAccountManagerResponse, str, z3, true, null);
        this.f638q = str2;
    }

    @Override // j3.e.f
    public final native void H3();

    @Override // j3.e.f
    public final native String I3(long j4);
}
