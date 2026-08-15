package r3;

import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import androidx.core.os.perationCompat;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    static {
        perationCompat.init0(741);
    }

    public static native String a(PackageManager packageManager, File file);

    public static native void b(ParcelFileDescriptor[] parcelFileDescriptorArr, File file);
}
