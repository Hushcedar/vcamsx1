package j;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class j implements AccountManagerCallback<Bundle> {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Intent f528b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AlertDialog f529c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f530d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final IBinder f531e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final String f532f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final int f533g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public HashSet f534h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public HashSet f535i;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public LinkedHashMap<Account, Integer> f538l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public ArrayList<Account> f539m;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f527a = 0;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public boolean f536j = false;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final HashSet f537k = new HashSet();

    static {
        perationCompat.init0(9);
    }

    public j(Intent intent, int i4, IBinder iBinder, String str, int i5) {
        this.f531e = null;
        this.f532f = null;
        this.f533g = -1;
        this.f528b = intent;
        this.f530d = i4;
        this.f531e = iBinder;
        this.f532f = str;
        this.f533g = i5;
    }

    public static native Activity a();

    @Override // android.accounts.AccountManagerCallback
    public final native void run(AccountManagerFuture<Bundle> accountManagerFuture);
}
