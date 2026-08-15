package m3;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Parcelable {
    public static final Parcelable.Creator<c> CREATOR;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final Account f1074e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1075a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Account f1076b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1077c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final long f1078d;

    public class a implements Parcelable.Creator<c> {
        static {
            perationCompat.init0(157);
        }

        @Override // android.os.Parcelable.Creator
        public final native c createFromParcel(Parcel parcel);

        @Override // android.os.Parcelable.Creator
        public final native c[] newArray(int i4);
    }

    static {
        perationCompat.init0(354);
        f1074e = new Account("*****", "*****");
        CREATOR = new a();
    }

    public c(int i4, Account account, String str, long j4) {
        this.f1075a = i4;
        this.f1076b = account;
        this.f1077c = str;
        this.f1078d = j4;
    }

    public c(Parcel parcel) {
        this.f1075a = parcel.readInt();
        this.f1076b = (Account) parcel.readParcelable(Account.class.getClassLoader());
        this.f1077c = parcel.readString();
        this.f1078d = parcel.readLong();
    }

    public c(c cVar) {
        this.f1075a = cVar.f1075a;
        Account account = cVar.f1076b;
        this.f1076b = new Account(account.name, account.type);
        this.f1077c = cVar.f1077c;
        this.f1078d = cVar.f1078d;
    }

    @Override // android.os.Parcelable
    public final native int describeContents();

    @Override // android.os.Parcelable
    public final native void writeToParcel(Parcel parcel, int i4);
}
