package x3;

import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class e extends a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f1869b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final List f1870c;

    public /* synthetic */ e(List list, int i4) {
        this.f1869b = i4;
        this.f1870c = list;
    }

    @Override // x3.b
    public final w3.a H(int i4) {
        String str;
        Parcelable parcelable;
        int i5 = this.f1869b;
        List list = this.f1870c;
        switch (i5) {
            case 0:
                if (list == null || list.isEmpty() || (parcelable = (Parcelable) list.remove(0)) == null) {
                    return null;
                }
                return new w3.b(parcelable);
            default:
                if (list == null || list.isEmpty() || (str = (String) list.remove(0)) == null) {
                    return null;
                }
                return new w3.c(str);
        }
    }

    @Override // x3.b
    public final int W() {
        int i4 = this.f1869b;
        List list = this.f1870c;
        switch (i4) {
        }
        return list.size();
    }
}
