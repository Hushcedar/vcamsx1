package j;

import android.content.DialogInterface;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class h implements DialogInterface.OnClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f524a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Object f525b;

    static {
        perationCompat.init0(4);
    }

    public /* synthetic */ h(int i4, Object obj) {
        this.f524a = i4;
        this.f525b = obj;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final native void onClick(DialogInterface dialogInterface, int i4);
}
