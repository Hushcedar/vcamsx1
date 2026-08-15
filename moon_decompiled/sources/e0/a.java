package e0;

import android.annotation.TargetApi;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(25)
public final class a extends c {

    /* JADX INFO: renamed from: e0.a$a, reason: collision with other inner class name */
    public static class C0015a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Object f324h;

        static {
            perationCompat.init0(426);
        }

        public C0015a(Object obj) {
            this.f324h = null;
            this.f324h = obj;
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public static class b extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public final boolean f326i = true;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final String f325h = "android.content.pm.IShortcutService";

        static {
            perationCompat.init0(430);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(674);
    }

    public a() {
        super("android.content.pm.IShortcutService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
