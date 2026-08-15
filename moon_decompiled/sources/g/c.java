package g;

import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final HashMap f374a = new HashMap(5);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final d f375b;

    static {
        perationCompat.init0(144);
    }

    public c(Object obj) {
        this.f375b = new d(obj, this);
    }

    public final native String toString();
}
