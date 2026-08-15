package j3;

import android.accounts.Account;
import android.util.LruCache;
import android.util.Pair;
import androidx.core.os.perationCompat;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final b f662a = new b();

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Account f663a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f664b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f665c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final byte[] f666d;

        static {
            perationCompat.init0(84);
        }

        public a(Account account, String str, String str2, byte[] bArr) {
            this.f663a = account;
            this.f665c = str;
            this.f664b = str2;
            this.f666d = bArr;
        }

        public final native boolean equals(Object obj);

        public final native int hashCode();
    }

    public static class b extends LruCache<a, c> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final HashMap<Pair<String, String>, a> f667a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final HashMap<Account, a> f668b;

        public class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final ArrayList f669a = new ArrayList();

            static {
                perationCompat.init0(313);
            }

            public a() {
            }

            public final native void a();
        }

        static {
            perationCompat.init0(82);
        }

        public b() {
            super(64000);
            this.f667a = new HashMap<>();
            this.f668b = new HashMap<>();
        }

        @Override // android.util.LruCache
        public final native void entryRemoved(boolean z3, a aVar, c cVar, c cVar2);

        @Override // android.util.LruCache
        public final native int sizeOf(a aVar, c cVar);
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f671a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final long f672b;

        public c(long j4, String str) {
            this.f671a = str;
            this.f672b = j4;
        }
    }

    static {
        perationCompat.init0(162);
    }

    public final native void a(Account account, String str, String str2, String str3, byte[] bArr, long j4);
}
