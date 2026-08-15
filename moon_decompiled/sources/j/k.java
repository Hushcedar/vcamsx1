package j;

import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class k {
    static {
        perationCompat.init0(14);
    }

    public static native Uri a(Intent intent, String str, int i4);

    public static native Uri b(String str, Uri uri, int i4);

    public static native Pair<Integer, String> c(Uri uri);
}
