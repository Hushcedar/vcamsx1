package j;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class b implements AccountManagerCallback<Bundle> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f488a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Intent f489b;

    static {
        perationCompat.init0(751);
    }

    public b(Intent intent) {
        this.f489b = intent;
    }

    public final native void a(String str);

    @Override // android.accounts.AccountManagerCallback
    public final native void run(AccountManagerFuture<Bundle> accountManagerFuture);
}
