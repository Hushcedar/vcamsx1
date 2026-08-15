package s3;

import android.content.Intent;
import android.content.pm.IPackageDeleteObserver2;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: s3.a$a, reason: collision with other inner class name */
    public class C0082a extends IPackageDeleteObserver2.Stub {
        static {
            perationCompat.init0(643);
        }

        public C0082a() {
        }

        public final native void onPackageDeleted(String str, int i4, String str2);

        public final native void onUserActionRequired(Intent intent);
    }

    public a() {
        new C0082a();
    }
}
