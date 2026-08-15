package v;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.io.File;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static f f1697f;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f1698a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Handler f1700c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public HandlerThread f1701d;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HashMap f1699b = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final a f1702e = new a();

    public class a extends v3.h {
        static {
            perationCompat.init0(29);
        }

        public a() {
            File file;
            f.this.getClass();
            try {
                file = new File(Environment.getExternalStorageDirectory(), ".hack_mediaprovider_" + f.this.f1698a.getPackageName());
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception unused) {
                file = null;
            }
            super(file);
        }

        @Override // v3.h
        public final native int c();

        @Override // v3.h
        public final native void e(Parcel parcel, int i4);

        @Override // v3.h
        public final native void g(Parcel parcel);
    }

    public class b extends Handler {
        static {
            perationCompat.init0(28);
        }

        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final native void handleMessage(Message message);
    }

    static {
        perationCompat.init0(189);
    }

    public f(Context context) {
        this.f1698a = context;
    }

    public static native f a(Context context);
}
