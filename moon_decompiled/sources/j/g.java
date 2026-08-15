package j;

import android.content.DialogInterface;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class g implements DialogInterface.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f521a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Object f522b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Object f523c;

    static {
        perationCompat.init0(6);
    }

    public /* synthetic */ g(Object obj, Object obj2, int i4) {
        this.f521a = i4;
        this.f522b = obj;
        this.f523c = obj2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final native void onClick(DialogInterface dialogInterface, int i4);
}
