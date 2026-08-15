package h1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: h1.a$a, reason: collision with other inner class name */
    public static class C0025a extends b {
        static {
            perationCompat.init0(575);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(120);
    }

    public a() {
        super("android.app.ISearchManager");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
