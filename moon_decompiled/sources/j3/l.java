package j3;

import android.accounts.AuthenticatorDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AtomicFile;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.os.perationCompat;
import j3.b;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import r3.y;

/* JADX INFO: loaded from: classes.dex */
public abstract class l<V> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f643a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f644b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f645c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final String f646d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final o<V> f647e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final Object f648f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final SparseArray<c<V>> f649g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public m<V> f650h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public Handler f651i;

    public class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ m f652a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ Object f653b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final /* synthetic */ int f654c;

        static {
            perationCompat.init0(739);
        }

        public a(m mVar, Object obj, int i4, boolean z3) {
            this.f652a = mVar;
            this.f653b = obj;
            this.f654c = i4;
        }

        @Override // java.lang.Runnable
        public final native void run();
    }

    public static class b<V> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final V f655a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ComponentInfo f656b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final ComponentName f657c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final int f658d;

        static {
            perationCompat.init0(737);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public b(int i4, AuthenticatorDescription authenticatorDescription, ServiceInfo serviceInfo, ComponentName componentName) {
            this.f655a = authenticatorDescription;
            this.f656b = serviceInfo;
            this.f657c = componentName;
            this.f658d = r3.h.M3().l2(componentName.getPackageName(), i4);
        }

        public final native String toString();
    }

    public static class c<V> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final HashMap f659a = new HashMap();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public HashMap f660b = null;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final boolean f661c = true;
    }

    static {
        perationCompat.init0(161);
    }

    public l(Context context) {
        b.a aVar = j3.b.f552j;
        this.f648f = new Object();
        SparseArray<c<V>> sparseArray = new SparseArray<>(2);
        this.f649g = sparseArray;
        this.f643a = context;
        this.f644b = "android.accounts.AccountAuthenticator";
        this.f645c = "android.accounts.AccountAuthenticator";
        this.f646d = "account-authenticator";
        this.f647e = aVar;
        File file = new File(p.a.l(), "registered_services");
        AtomicFile atomicFile = new AtomicFile(new File(file, "android.accounts.AccountAuthenticator.xml"));
        if (atomicFile.getBaseFile().exists()) {
            File file2 = new File(file, "android.accounts.AccountAuthenticator.xml.migrated");
            if (file2.exists()) {
                return;
            }
            FileInputStream fileInputStreamOpenRead = null;
            try {
                try {
                    fileInputStreamOpenRead = atomicFile.openRead();
                    sparseArray.clear();
                    k(fileInputStreamOpenRead);
                } catch (Exception e4) {
                    Log.w("PackageManager", "Error reading persistent services, starting from scratch", e4);
                }
                try {
                    for (y yVar : r3.i.b().d(true)) {
                        c<V> cVar = sparseArray.get(yVar.f1454a);
                        if (cVar != null) {
                            l(cVar, yVar.f1454a);
                        }
                    }
                    file2.createNewFile();
                } catch (Exception e5) {
                    Log.w("PackageManager", "Migration failed", e5);
                }
                sparseArray.clear();
            } finally {
                k2.d.a(fileInputStreamOpenRead);
            }
        }
    }

    public final native AtomicFile b(int i4);

    public final native c<V> c(int i4, boolean z3);

    public final native void d(int[] iArr, int i4);

    public final native Collection<b<V>> e(int i4);

    public final native void f(int i4);

    public final native void g(V v4, int i4, boolean z3);

    public abstract AuthenticatorDescription h(Resources resources, String str, AttributeSet attributeSet);

    public final native b<V> i(ResolveInfo resolveInfo, int i4);

    public final native ArrayList j(int i4);

    public final native void k(FileInputStream fileInputStream);

    public final native void l(c<V> cVar, int i4);
}
