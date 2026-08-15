package q1;

import android.os.Parcel;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import u1.j;

/* JADX INFO: loaded from: classes.dex */
public final class c extends com.core.hack.handle.c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final HashMap f1289e;

    public class a extends com.core.hack.handle.b {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public int f1290h = -1;

        static {
            perationCompat.init0(36);
        }

        public a() {
        }

        @Override // com.core.hack.handle.b
        public final native String c();

        @Override // com.core.hack.handle.b
        public final native void h(j[] jVarArr, j[] jVarArr2);

        @Override // com.core.hack.handle.b
        public final native boolean l(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    static {
        perationCompat.init0(703);
    }

    public c() {
        super("android.view.IWindowSession");
        HashMap map = new HashMap();
        this.f1289e = map;
        map.put("android.view.InsetsVisibilities", Boolean.FALSE);
        Boolean bool = Boolean.TRUE;
        map.put("android.view.InputChannel", bool);
        map.put("android.view.InsetsState", bool);
        map.put("android.view.InsetsSourceControl", bool);
        map.put("android.util.MergedConfiguration", bool);
        map.put("android.window.ClientWindowFrames", bool);
        map.put("android.view.DisplayCutout$ParcelableWrapper", bool);
    }

    @Override // com.core.hack.handle.c
    public final native HashMap<String, com.core.hack.handle.b> c();
}
