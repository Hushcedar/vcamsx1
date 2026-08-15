package r3;

import android.os.ParcelFileDescriptor;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1388a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1389b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1390c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final String f1391d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final ParcelFileDescriptor[] f1392e;

    static {
        perationCompat.init0(743);
    }

    public l(int i4, int i5, String str, ParcelFileDescriptor[] parcelFileDescriptorArr) {
        this(i4, (String) null, str, i5);
        this.f1392e = parcelFileDescriptorArr;
    }

    public l(int i4, String str, String str2, int i5) {
        this.f1388a = i4;
        this.f1389b = i5;
        this.f1390c = str;
        this.f1391d = str2;
    }

    public final native String toString();
}
