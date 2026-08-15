package j;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.IAccountManagerResponse;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.perationCompat;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public abstract class d extends FutureTask<Bundle> implements AccountManagerFuture<Bundle> {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final /* synthetic */ int f490f = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final a f491a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Handler f492b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final AccountManagerCallback<Bundle> f493c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Activity f494d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final Handler f495e;

    public class a extends IAccountManagerResponse.Stub {
        static {
            perationCompat.init0(196);
        }

        public a() {
        }

        public final native void onError(int i4, String str);

        public final native void onResult(Bundle bundle);
    }

    static {
        perationCompat.init0(753);
    }

    public d(AccountManagerCallback accountManagerCallback) {
        super(new c());
        this.f492b = null;
        this.f493c = accountManagerCallback;
        this.f494d = null;
        this.f491a = new a();
        this.f495e = new Handler(Looper.getMainLooper());
    }

    public abstract void b();

    public final native Bundle c(Long l4, TimeUnit timeUnit);

    @Override // java.util.concurrent.FutureTask
    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public final native void set(Bundle bundle);

    @Override // java.util.concurrent.FutureTask
    public final native void done();

    public final native void e();

    @Override // android.accounts.AccountManagerFuture
    public final native Bundle getResult();

    @Override // android.accounts.AccountManagerFuture
    public final native Bundle getResult(long j4, TimeUnit timeUnit);
}
