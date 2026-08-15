package j;

import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.os.Bundle;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class a extends d {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final /* synthetic */ String f483g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final /* synthetic */ String f484h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final /* synthetic */ String[] f485i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final /* synthetic */ Activity f486j = null;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final /* synthetic */ Bundle f487k;

    static {
        perationCompat.init0(752);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(AccountManagerCallback accountManagerCallback, String str, String str2, String[] strArr, Bundle bundle) {
        super(accountManagerCallback);
        this.f483g = str;
        this.f484h = str2;
        this.f485i = strArr;
        this.f487k = bundle;
    }

    @Override // j.d
    public final native void b();
}
