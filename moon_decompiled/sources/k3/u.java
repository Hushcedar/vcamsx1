package k3;

import android.util.ArrayMap;
import android.util.SparseArray;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class u<E> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final ArrayMap<String, SparseArray<E>> f932a = new ArrayMap<>();

    static {
        perationCompat.init0(304);
    }

    public final native Object a(int i4, String str);
}
