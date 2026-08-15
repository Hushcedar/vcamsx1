package k3;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class y {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final SparseArray<HashMap<String, String>> f992a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final SparseArray<HashMap<String, String>> f993b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseArray<HashMap<String, String>> f994c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final SparseArray<HashMap<String, String>> f995d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final SparseArray[] f996e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final ContentResolver f997f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public HandlerThread f998g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public Handler f999h;

    public class a extends Handler {
        static {
            perationCompat.init0(177);
        }

        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    static {
        perationCompat.init0(259);
    }

    public y(Context context) {
        SparseArray<HashMap<String, String>> sparseArray = new SparseArray<>();
        this.f992a = sparseArray;
        SparseArray<HashMap<String, String>> sparseArray2 = new SparseArray<>();
        this.f993b = sparseArray2;
        SparseArray<HashMap<String, String>> sparseArray3 = new SparseArray<>();
        this.f994c = sparseArray3;
        SparseArray<HashMap<String, String>> sparseArray4 = new SparseArray<>();
        this.f995d = sparseArray4;
        this.f996e = new SparseArray[]{sparseArray, sparseArray2, sparseArray3, sparseArray4};
        this.f997f = context.getContentResolver();
    }

    public static native void a(y yVar, int i4);

    public static native void c(HashMap<String, String> map, Parcel parcel);

    public final native void b(int i4);
}
