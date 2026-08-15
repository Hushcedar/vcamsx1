package n1;

import android.annotation.SuppressLint;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.b;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: n1.a$a, reason: collision with other inner class name */
    public class C0064a extends b {
        static {
            perationCompat.init0(34);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        @SuppressLint({"NewApi"})
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        @SuppressLint({"NewApi"})
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(578);
    }

    public a() {
        super("android.service.textclassifier.ITextClassifierService");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, b> c();
}
