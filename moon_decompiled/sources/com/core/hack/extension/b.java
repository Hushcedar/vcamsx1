package com.core.hack.extension;

import android.app.ClientTransactionHandler;
import android.app.servertransaction.ClientTransactionItem;
import android.app.servertransaction.PendingTransactionActions;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class b extends ClientTransactionItem {
    public static final Parcelable.Creator<b> CREATOR;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final boolean f250b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f251c = "b";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ClientTransactionItem f252a;

    public class a implements Parcelable.Creator<b> {
        static {
            perationCompat.init0(207);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public native b createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public native b[] newArray(int i4);
    }

    static {
        perationCompat.init0(429);
        CREATOR = new a();
    }

    private b() {
    }

    public static native b a(Object obj);

    public native void execute(ClientTransactionHandler clientTransactionHandler, IBinder iBinder, PendingTransactionActions pendingTransactionActions);

    public native void postExecute(ClientTransactionHandler clientTransactionHandler, IBinder iBinder, PendingTransactionActions pendingTransactionActions);

    public native void preExecute(ClientTransactionHandler clientTransactionHandler, IBinder iBinder);

    public native void writeToParcel(Parcel parcel, int i4);
}
