package f0;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.lang.reflect.Method;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: f0.a$a, reason: collision with other inner class name */
    public static class C0019a extends com.core.hack.handle.client.c {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public int f357m;

        static {
            perationCompat.init0(195);
        }

        public C0019a() {
            super("android.app.usage.IUsageStatsManager");
            this.f357m = -1;
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends C0019a {
        static {
            perationCompat.init0(200);
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(37);
    }

    public a() {
        super("android.app.usage.IUsageStatsManager");
        d();
    }

    @Override // com.core.hack.handle.c
    public final native com.core.hack.handle.b b(Method method);

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
