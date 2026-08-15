package z2;

import java.io.File;
import v1.c;
import v1.d;
import v1.e;
import v1.h;
import v1.i;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @d(name = "copyNativeBinaries", value = {@i(strings = {"com.android.internal.content.NativeLibraryHelper$Handle"}, type = 1), @i(classes = {File.class, String.class})})
    public static h<Integer> f1908a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @d(name = "findSupportedAbi", value = {@i(strings = {"com.android.internal.content.NativeLibraryHelper$Handle"}, type = 1), @i(classes = {String[].class})})
    public static h<Integer> f1909b;

    /* JADX INFO: renamed from: z2.a$a, reason: collision with other inner class name */
    public static class C0103a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @e(name = "create", value = {File.class})
        public static h<Object> f1910a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @v1.b(name = "extractNativeLibs")
        public static c<Boolean> f1911b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @v1.b(name = "multiArch")
        public static c<Boolean> f1912c;

        static {
            j.e.q(C0103a.class, "com.android.internal.content.NativeLibraryHelper$Handle");
        }
    }

    static {
        j.e.q(a.class, "com.android.internal.content.NativeLibraryHelper");
    }
}
