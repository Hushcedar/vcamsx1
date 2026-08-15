package r1;

import android.os.Binder;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class a extends c {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final Method f1301f;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final Object f1302e;

    static {
        perationCompat.init0(85);
        try {
            Class cls = Integer.TYPE;
            Method declaredMethod = Binder.class.getDeclaredMethod("onTransact", cls, Parcel.class, Parcel.class, cls);
            f1301f = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (Exception unused) {
        }
    }

    public a(Object obj) {
        super("android.app.IApplicationThread");
        this.f1302e = obj;
    }

    public final native boolean g(int i4, Parcel parcel, Parcel parcel2, int i5);
}
