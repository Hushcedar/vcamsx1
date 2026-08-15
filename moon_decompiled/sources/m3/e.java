package m3;

import android.accounts.Account;
import android.content.Context;
import android.content.ISyncStatusObserver;
import android.content.Intent;
import android.content.SyncStatusInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public final class e extends Handler {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final HashMap<String, String> f1080n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final Intent f1081o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static volatile e f1082p;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final SparseArray<c> f1083a = new SparseArray<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final HashMap<j3.a, b> f1084b = new HashMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseArray<ArrayList<m3.c>> f1085c = new SparseArray<>();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final SparseArray<SyncStatusInfo> f1086d = new SparseArray<>();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final RemoteCallbackList<ISyncStatusObserver> f1087e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public int f1088f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final d[] f1089g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public final Context f1090h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f1091i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public final AtomicFile f1092j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public final AtomicFile f1093k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public final AtomicFile f1094l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public final SparseArray<Boolean> f1095m;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final i.b f1096a = i.b.h();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final SparseArray<Account[]> f1097b = new SparseArray<>();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final SparseArray<ArrayMap<String, Boolean>> f1098c = new SparseArray<>();
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final HashMap<String, c> f1099a = new HashMap<>();
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final C0061e f1100a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f1101b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public boolean f1102c = false;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public final ArrayList<m3.b> f1105f = new ArrayList<>();

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f1103d = -1;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public long f1104e = -1;

        static {
            perationCompat.init0(654);
        }

        public c(C0061e c0061e, int i4) {
            this.f1100a = c0061e;
            this.f1101b = i4;
            HashMap<String, String> map = e.f1080n;
        }

        public final native String toString();
    }

    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f1106a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f1107b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public long f1108c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f1109d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public long f1110e;

        public d(int i4) {
            this.f1106a = i4;
        }
    }

    /* JADX INFO: renamed from: m3.e$e, reason: collision with other inner class name */
    public static class C0061e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Account f1111a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f1112b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f1113c;

        static {
            perationCompat.init0(652);
        }

        public C0061e(Account account, String str, int i4) {
            this.f1111a = account;
            this.f1113c = str;
            this.f1112b = i4;
        }

        public final native boolean a(C0061e c0061e);

        public final native String toString();
    }

    static {
        perationCompat.init0(359);
        f1081o = new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");
        HashMap<String, String> map = new HashMap<>();
        f1080n = map;
        map.put("contacts", "com.android.contacts");
        map.put("calendar", "com.android.calendar");
        f1082p = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x02c9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x00fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x02ad A[EXC_TOP_SPLITTER, PHI: r3
      0x02ad: PHI (r3v15 java.io.FileInputStream) = (r3v13 java.io.FileInputStream), (r3v14 java.io.FileInputStream), (r3v30 java.io.FileInputStream) binds: [B:133:0x02ab, B:128:0x029c, B:16:0x00cd] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0112 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0154 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x02da A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x02c3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0334 A[ADDED_TO_REGION, EDGE_INSN: B:266:0x0334->B:158:0x0334 BREAK  A[LOOP:2: B:145:0x02f6->B:269:0x02f6], REMOVE, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x011f A[Catch: all -> 0x0278, IOException -> 0x027b, XmlPullParserException -> 0x027d, TryCatch #14 {IOException -> 0x027b, XmlPullParserException -> 0x027d, all -> 0x0278, blocks: (B:8:0x00a8, B:12:0x00be, B:18:0x00d1, B:20:0x00dd, B:23:0x00ec, B:26:0x00f2, B:30:0x0100, B:29:0x00fc, B:31:0x0108, B:35:0x0116, B:38:0x011b, B:40:0x011f, B:41:0x0131, B:43:0x0135, B:48:0x013f, B:34:0x0112, B:37:0x0119), top: B:256:0x00a8 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x013e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public e(android.content.Context r36, java.io.File r37) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1468
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: m3.e.<init>(android.content.Context, java.io.File):void");
    }

    public static native long a(long j4);

    public static native void j(XmlPullParser xmlPullParser, Bundle bundle);

    public static native m3.b l(XmlPullParser xmlPullParser, c cVar);

    public final native c b(int i4);

    public final native ArrayList c(int i4);

    public final native ArrayList d(int i4, boolean z3);

    public final native c e(C0061e c0061e, int i4);

    public final native boolean f(Account account, String str, int i4);

    public final native boolean g(C0061e c0061e);

    public final native boolean h(C0061e c0061e);

    @Override // android.os.Handler
    public final native void handleMessage(Message message);

    public final native c i(XmlPullParser xmlPullParser, int i4, a aVar);

    public final native void k(XmlPullParser xmlPullParser);

    public final native void m();

    public final native void n(int i4, int i5, Account account, String str);

    public final native void o(int i4, boolean z3);

    public final native void p(Account account, String str, boolean z3, int i4);

    public final native void q();

    public final native void r();

    public final native void s();
}
