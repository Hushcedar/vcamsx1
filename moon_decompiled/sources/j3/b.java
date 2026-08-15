package j3;

import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import androidx.core.os.perationCompat;
import j3.l;

/* JADX INFO: loaded from: classes.dex */
public final class b extends l<AuthenticatorDescription> implements r3.j {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f552j;

    public static class a implements o<AuthenticatorDescription> {
    }

    static {
        perationCompat.init0(172);
        f552j = new a();
    }

    public b(Context context) {
        super(context);
    }

    @Override // r3.j
    public final native void a(int i4, int i5, int[] iArr, String str);

    @Override // j3.l
    public final native AuthenticatorDescription h(Resources resources, String str, AttributeSet attributeSet);

    public final native l.b m(AuthenticatorDescription authenticatorDescription, int i4);
}
