package i3;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.ArrayMap;
import android.util.LruCache;
import android.util.SparseArray;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static c f447c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f448a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final LruCache<String, b> f449b = new LruCache<>(4);

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final TypedArray f450a;

        public a(TypedArray typedArray) {
            this.f450a = typedArray;
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Resources.Theme f451a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final SparseArray<ArrayMap<int[], a>> f452b = new SparseArray<>();

        public b(Resources resources) {
            this.f451a = resources.newTheme();
        }
    }

    static {
        perationCompat.init0(66);
    }

    public c(Context context) {
        this.f448a = context;
    }

    public final native a a(String str, String str2, int i4, int[] iArr);
}
