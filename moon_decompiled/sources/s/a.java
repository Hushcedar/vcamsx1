package s;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import com.core.hack.handle.c;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c {

    /* JADX INFO: renamed from: s.a$a, reason: collision with other inner class name */
    public static class C0079a extends com.core.hack.handle.client.c {
        static {
            perationCompat.init0(625);
        }

        public C0079a() {
            super("com.android.internal.appwidget.IAppWidgetService");
        }

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);
    }

    public static class b extends C0079a {

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public final Object f1460m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public final j f1461n;

        static {
            perationCompat.init0(622);
        }

        public b(Object obj, j jVar) {
            this.f1460m = obj;
            this.f1461n = jVar;
        }

        @Override // com.core.hack.handle.client.c, com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

        @Override // com.core.hack.handle.client.c
        public final native boolean p();
    }

    static {
        perationCompat.init0(348);
    }

    public a() {
        super("com.android.internal.appwidget.IAppWidgetService");
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
