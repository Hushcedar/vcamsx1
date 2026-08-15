package f;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.core.os.perationCompat;
import i.i;
import java.util.ArrayList;
import java.util.List;
import t1.g;

/* JADX INFO: loaded from: classes.dex */
public interface b extends IInterface {

    public static abstract class a extends Binder implements b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f333a = 0;

        /* JADX INFO: renamed from: f.b$a$a, reason: collision with other inner class name */
        public static class C0018a implements b {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final IBinder f334a;

            static {
                perationCompat.init0(722);
            }

            public C0018a(IBinder iBinder) {
                this.f334a = iBinder;
            }

            @Override // f.b
            public final native void C2(IBinder iBinder, Intent intent, int i4, String str, Bundle bundle, boolean z3, boolean z4, int i5);

            @Override // f.b
            public final native boolean D0(Intent intent, i iVar, int i4);

            @Override // f.b
            public final native void F1(String str, g gVar);

            @Override // f.b
            public final native void G2();

            @Override // f.b
            public final native boolean H1(i iVar, int i4);

            @Override // f.b
            public final native void M0(i3.e eVar);

            @Override // f.b
            public final native void Q1(IBinder iBinder);

            @Override // f.b
            public final native void R1(IBinder iBinder, Intent intent, boolean z3, int i4);

            @Override // android.os.IInterface
            public final native IBinder asBinder();

            @Override // f.b
            public final native void g0(Intent intent, ActivityInfo activityInfo);

            @Override // f.b
            public final native void g3(IBinder iBinder, ServiceInfo serviceInfo, int i4);

            @Override // f.b
            public final native void p1(String str, ApplicationInfo applicationInfo, List<ProviderInfo> list, ComponentName componentName);

            @Override // f.b
            public final native void q0(String str, c cVar, int i4);

            @Override // f.b
            public final native void q2(IBinder iBinder, ArrayList arrayList);

            @Override // f.b
            public final native void r2(IBinder iBinder, Intent intent);

            @Override // f.b
            public final native boolean v(ProviderInfo providerInfo);

            @Override // f.b
            public final native IBinder z(ProviderInfo providerInfo, boolean z3);
        }

        static {
            perationCompat.init0(562);
        }

        public a() {
            attachInterface(this, "com.core.hack.client.core.IApplicationThread");
        }

        public static native b E3(IBinder iBinder);

        @Override // android.os.IInterface
        public final native IBinder asBinder();

        @Override // android.os.Binder
        public final native boolean onTransact(int i4, Parcel parcel, Parcel parcel2, int i5);
    }

    void C2(IBinder iBinder, Intent intent, int i4, String str, Bundle bundle, boolean z3, boolean z4, int i5);

    boolean D0(Intent intent, i iVar, int i4);

    void F1(String str, g gVar);

    void G2();

    boolean H1(i iVar, int i4);

    void M0(i3.e eVar);

    void Q1(IBinder iBinder);

    void R1(IBinder iBinder, Intent intent, boolean z3, int i4);

    void g0(Intent intent, ActivityInfo activityInfo);

    void g3(IBinder iBinder, ServiceInfo serviceInfo, int i4);

    void p1(String str, ApplicationInfo applicationInfo, List<ProviderInfo> list, ComponentName componentName);

    void q0(String str, c cVar, int i4);

    void q2(IBinder iBinder, ArrayList arrayList);

    void r2(IBinder iBinder, Intent intent);

    boolean v(ProviderInfo providerInfo);

    IBinder z(ProviderInfo providerInfo, boolean z3);
}
