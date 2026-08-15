package s3;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.Checksum;
import android.content.pm.DataLoaderParamsParcel;
import android.content.pm.IPackageInstallerSession;
import android.content.pm.PackageInstaller;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.util.SparseIntArray;
import androidx.core.os.perationCompat;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.xmlpull.v1.XmlPullParser;
import s3.b;
import s3.e;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(21)
public final class i extends IPackageInstallerSession.Stub {
    public static final int[] G;
    public static final a H;
    public b.a A;
    public File C;
    public String D;
    public File E;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final e.b f1509a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Context f1510b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Handler f1511c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final r3.h f1512d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f1513e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final int f1514f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final int f1515g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final PackageInstaller.SessionParams f1516h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public final String f1517i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final boolean f1518j;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public int f1520l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final j f1521m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final long f1522n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public final long f1523o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public final File f1524p;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public boolean f1531w;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public boolean f1534z;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final SparseIntArray f1519k = new SparseIntArray();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public final AtomicInteger f1525q = new AtomicInteger();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public final Object f1526r = new Object();

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    public float f1527s = 0.0f;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public float f1528t = 0.0f;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public float f1529u = 0.0f;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    public float f1530v = -1.0f;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public boolean f1532x = false;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public boolean f1533y = false;
    public final ArrayList<t1.d> B = new ArrayList<>();
    public final ArrayList F = new ArrayList();

    public class a implements FileFilter {
        static {
            perationCompat.init0(242);
        }

        @Override // java.io.FileFilter
        public final native boolean accept(File file);
    }

    public class b implements Handler.Callback {
        static {
            perationCompat.init0(246);
        }

        public b() {
        }

        @Override // android.os.Handler.Callback
        public final native boolean handleMessage(Message message);
    }

    public static class c extends Exception {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f1536a;

        public c(int i4, String str) {
            super(str);
            this.f1536a = i4;
        }

        public c(Throwable th) {
            super(th);
            this.f1536a = -110;
        }
    }

    static {
        perationCompat.init0(21);
        G = new int[0];
        H = new a();
    }

    public i(e.b bVar, Context context, r3.h hVar, j jVar, Looper looper, int i4, int i5, String str, int i6, PackageInstaller.SessionParams sessionParams, long j4, File file, boolean z3, boolean z4, boolean z5, int[] iArr, int i7) {
        this.f1518j = false;
        this.f1520l = -1;
        this.f1531w = false;
        this.f1534z = false;
        b bVar2 = new b();
        this.f1509a = bVar;
        this.f1510b = context;
        this.f1511c = new Handler(looper, bVar2);
        this.f1512d = hVar;
        this.f1517i = str;
        this.f1513e = i4;
        this.f1514f = i5;
        this.f1515g = i6;
        this.f1516h = sessionParams;
        this.D = c2.g.g(sessionParams);
        this.f1518j = c2.g.o(sessionParams);
        this.f1524p = file;
        this.f1521m = jVar;
        this.f1523o = j4;
        this.f1522n = j4;
        if (iArr != null) {
            for (int i8 : iArr) {
                this.f1519k.put(i8, 0);
            }
        }
        this.f1520l = i7;
        this.f1531w = z3;
        this.f1534z = z4;
    }

    public static native void E3(i iVar);

    public static native String getCompleteMessage(Throwable th);

    public static native boolean isDexMetadataFile(File file);

    public static native i readFromXml(XmlPullParser xmlPullParser, e.b bVar, Context context, r3.h hVar, Looper looper, File file, j jVar);

    public final native void F3(String str);

    public final native void G3(String str);

    public final native void H3();

    public final native void I3();

    public final native void J3(boolean z3);

    public final native void K3(String str);

    public final native void L3();

    public final native void M3(String str, int i4);

    public final native ArrayList N3();

    public final native boolean O3();

    public final native ParcelFileDescriptor P3(String str);

    public final native ParcelFileDescriptor Q3(String str, long j4, long j5);

    public final native File R3();

    public final native void S3(int i4);

    public final native void T3(boolean z3);

    public final native void U3(j3.d dVar, File file);

    public native void abandon();

    public native void addChildSessionId(int i4);

    public native void addClientProgress(float f4);

    public native void close();

    public native void commit(IntentSender intentSender);

    @TargetApi(26)
    public native void commit(IntentSender intentSender, boolean z3);

    public native List<String> fetchPackageNames();

    public native PackageInstaller.SessionInfo generateInfo();

    public native PackageInstaller.SessionInfo generateInfo(boolean z3);

    public native ParcelFileDescriptor getAppMetadataFd();

    public native int[] getChildSessionIds();

    public native DataLoaderParamsParcel getDataLoaderParams();

    public native int getInstallFlags();

    public native int getInstallerUid();

    public native String[] getNames();

    public native int getParentSessionId();

    public final native List<i> getSelfOrChildSessions();

    public native long getUpdatedMillis();

    public native boolean isApplicationEnabledSettingPersistent();

    public native boolean isMultiPackage();

    public native boolean isPrepared();

    public native boolean isRequestUpdateOwnership();

    public native boolean isSealed();

    public native boolean isStaged();

    public native boolean isStagedAndInTerminalState();

    public native boolean markAsCommitted(IntentSender intentSender, boolean z3);

    public native void open();

    public native ParcelFileDescriptor openRead(String str);

    public native ParcelFileDescriptor openWrite(String str, long j4, long j5);

    public native ParcelFileDescriptor openWriteAppMetadata();

    public native void removeAppMetadata();

    public native void removeChildSessionId(int i4);

    public native void removeSplit(String str);

    public native void setChecksums(String str, Checksum[] checksumArr, byte[] bArr);

    public native void setClientProgress(float f4);

    public native String toString();

    public native void writeToParcel(Parcel parcel, int i4);
}
