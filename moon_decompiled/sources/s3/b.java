package s3;

import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.os.Bundle;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final a f1480a = new a();

    public class a extends IPackageInstallObserver2.Stub {
        static {
            perationCompat.init0(599);
        }

        public a() {
        }

        public final native void onPackageInstalled(String str, int i4, String str2, Bundle bundle);

        public final native void onUserActionRequired(Intent intent);
    }
}
