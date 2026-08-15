package k0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: k0.a$a, reason: collision with other inner class name */
    public class C0044a extends com.core.hack.handle.b {
        static {
            perationCompat.init0(327);
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public class b extends C0044a {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Integer f675h;

        static {
            perationCompat.init0(325);
        }

        public b(Integer num) {
            this.f675h = num;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class c extends C0044a {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public final Long f677h;

        static {
            perationCompat.init0(329);
        }

        public c(Long l4) {
            this.f677h = l4;
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    public class d extends C0044a {
        static {
            perationCompat.init0(328);
        }

        public d() {
        }

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(352);
    }

    public a() {
        super("android.app.backup.IBackupManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
