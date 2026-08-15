package h0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: h0.a$a, reason: collision with other inner class name */
    public class C0024a extends b {
        static {
            perationCompat.init0(612);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean k(int i4, Parcel parcel, Parcel parcel2, int i5, boolean z3);
    }

    static {
        perationCompat.init0(170);
    }

    public a() {
        super("android.webkit.IWebViewUpdateService");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
