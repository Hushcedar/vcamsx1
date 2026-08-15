package p3;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import i.o;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import k3.p;
import v3.h;
import v3.k;

/* JADX INFO: loaded from: classes.dex */
public final class c extends o.a {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f1205g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1206a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final HashMap<String, Set<p3.b>> f1207b = new HashMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1208c = 10000;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public JobScheduler f1209d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public p f1210e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public b f1211f;

    public class a extends k<c> {
        static {
            perationCompat.init0(22);
        }

        @Override // v3.k
        public final native c a();
    }

    public class b extends h {
        static {
            perationCompat.init0(23);
        }

        public b() {
            super(new File(p.a.l(), "jobs.list"));
        }

        @Override // v3.h
        public final native void e(Parcel parcel, int i4);

        @Override // v3.h
        public final native void g(Parcel parcel);
    }

    static {
        perationCompat.init0(613);
        f1205g = new a();
    }

    public static native c I3();

    public static native String L3(int i4, String str);

    public final native void H3(p3.b bVar, String str, int i4);

    public final native p3.b J3(int i4);

    public final native Set K3(int i4, String str);

    @Override // i.o.a, i.o
    public final native JobInfo W1(int i4, String str, int i5);

    @Override // i.o.a, i.o
    public final native int f2(JobInfo jobInfo, String str, int i4);

    @Override // i.o.a, i.o
    public final native List l3(String str, int i4);

    @Override // i.o.a, i.o
    public final native int n3(int i4, String str, int i5);

    @Override // i.o.a, i.o
    public final native void p0(String str, int i4);
}
