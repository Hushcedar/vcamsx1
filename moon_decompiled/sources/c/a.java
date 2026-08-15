package c;

import android.os.Binder;
import android.os.Parcel;
import com.core.Cmd;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static native void aman();

    public static native boolean apr(int i4, String str, String str2, int i5);

    public static native void bhe();

    public static native void bhs();

    public static native void bman();

    public static native int cara(String str);

    public static native boolean cjcn(String str, boolean z3);

    public static native boolean con(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5);

    public static native int cpn();

    public static native void dj();

    public static native void dvib();

    public static native void fbcu();

    public static native long gec32();

    public static native String gnp(String str, int i4);

    public static native String gopn();

    public static native int gpt();

    private static Object of(int i4, Object obj) {
        return Cmd.exec(i4, obj);
    }

    private static boolean ot(Object obj, int i4, Parcel parcel, Parcel parcel2, int i5) {
        return a.a.a(obj, i4, parcel, parcel2, i5);
    }

    private static boolean otx(Object obj, int i4, Parcel parcel, Parcel parcel2, boolean z3, int i5) {
        return a.a.c(obj, i4, parcel, parcel2, z3, i5);
    }

    public static native void pih(String str, int i4, int i5);

    public static native long rbsn(Binder binder, Binder binder2);

    public static native boolean sSb();

    public static native void sai(String str, int i4, int i5);

    public static native void sc();

    public static native void sibt(boolean z3);

    public static native void spi();

    public static native boolean sspc(String str, String str2);

    public static native void stdmn();
}
