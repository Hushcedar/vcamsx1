package j3;

import android.accounts.Account;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Account f550a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f551b;

    static {
        perationCompat.init0(171);
    }

    public a(int i4, Account account) {
        this.f550a = account;
        this.f551b = i4;
    }

    public final native boolean equals(Object obj);

    public final native int hashCode();

    public final native String toString();
}
