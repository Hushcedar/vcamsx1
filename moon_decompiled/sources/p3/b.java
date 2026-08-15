package p3;

import android.app.job.JobInfo;
import android.os.Parcel;
import androidx.core.os.perationCompat;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1201a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1202b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f1203c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final JobInfo f1204d;

    static {
        perationCompat.init0(616);
    }

    public b(Parcel parcel) {
        this.f1201a = parcel.readString();
        this.f1202b = parcel.readInt();
        this.f1203c = parcel.readInt();
        this.f1204d = (JobInfo) parcel.readParcelable(Parcel.class.getClassLoader());
    }

    public b(String str, int i4, JobInfo jobInfo, int i5) {
        this.f1201a = str;
        this.f1202b = i4;
        this.f1204d = jobInfo;
        this.f1203c = i5;
    }

    public final native String toString();
}
