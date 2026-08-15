package b3;

import v1.e;
import v1.h;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @e(name = "newUnpaddedIntArray", value = {int.class})
    private static h<int[]> f78a;

    static {
        j.e.q(a.class, "com.android.internal.util.ArrayUtils");
    }

    public static int[] a(int i4) {
        h<int[]> hVar = f78a;
        return hVar != null ? hVar.a(new Object[]{Integer.valueOf(i4)}) : new int[i4];
    }
}
