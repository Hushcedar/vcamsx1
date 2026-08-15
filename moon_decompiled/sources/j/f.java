package j;

import android.content.DialogInterface;
import androidx.core.os.perationCompat;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class f implements DialogInterface.OnMultiChoiceClickListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f517a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Map f518b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String[] f519c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ Object f520d;

    static {
        perationCompat.init0(0);
    }

    public /* synthetic */ f(Object obj, HashMap map, String[] strArr, int i4) {
        this.f517a = i4;
        this.f520d = obj;
        this.f518b = map;
        this.f519c = strArr;
    }

    @Override // android.content.DialogInterface.OnMultiChoiceClickListener
    public final native void onClick(DialogInterface dialogInterface, int i4, boolean z3);
}
