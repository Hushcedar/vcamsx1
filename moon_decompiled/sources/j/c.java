package j;

import android.os.Bundle;
import androidx.core.os.perationCompat;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Callable<Bundle> {
    static {
        perationCompat.init0(754);
    }

    @Override // java.util.concurrent.Callable
    public final native Bundle call();
}
