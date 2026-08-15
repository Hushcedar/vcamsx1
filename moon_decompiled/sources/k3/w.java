package k3;

import android.content.IIntentReceiver;
import android.os.IBinder;
import androidx.core.os.perationCompat;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class w extends ArrayList<j> implements IBinder.DeathRecipient {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final IIntentReceiver f958a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final v f959b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f960c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f961d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f962e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public String f963f;

    static {
        perationCompat.init0(255);
    }

    public w(v vVar, int i4, int i5, int i6, IIntentReceiver iIntentReceiver) {
        this.f958a = iIntentReceiver;
        this.f959b = vVar;
        this.f960c = i4;
        this.f961d = i5;
        this.f962e = i6;
    }

    @Override // android.os.IBinder.DeathRecipient
    public final native void binderDied();

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.Collection, java.util.List
    public final native boolean equals(Object obj);

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.Collection, java.util.List
    public final native int hashCode();

    @Override // java.util.AbstractCollection
    public final native String toString();
}
