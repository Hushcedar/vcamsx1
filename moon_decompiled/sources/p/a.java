package p;

import android.util.AndroidRuntimeException;
import f.e;
import java.io.File;
import t1.f;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static File f1180a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static File f1181b;

    public static void a(int i4, String str, int i5) {
        f.f(h(i4, str).getAbsolutePath());
        f.f(g(i4, str).getAbsolutePath());
        f.f(j(i4, str).getAbsolutePath());
        f.f(b(i4, str).getAbsolutePath());
        if ((i5 & 1) != 0) {
            File file = new File(k(i4), "/Android/obb/" + str);
            if (!file.exists()) {
                file.mkdirs();
            }
            f.f(file.getAbsolutePath());
        }
    }

    public static File b(int i4, String str) {
        File file = new File(k(i4), "/Android/media/" + str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File c() {
        if (f1180a == null) {
            e.e().m();
            n();
            if (f1180a == null) {
                throw new AndroidRuntimeException("must init base dir before.");
            }
        }
        File file = new File(f1180a, "data");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String d() {
        if (f1180a == null) {
            e.e().m();
            n();
            if (f1180a == null) {
                throw new AndroidRuntimeException("must init base dir before.");
            }
        }
        return f1180a.getAbsolutePath();
    }

    public static File e(String str) {
        File file = new File(c(), "app");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2;
    }

    public static File f(String str) {
        StringBuilder sb = new StringBuilder();
        String str2 = File.separator;
        sb.append(str2);
        sb.append("data");
        sb.append(str2);
        sb.append("app");
        sb.append(str2);
        sb.append(str);
        return new File(sb.toString());
    }

    public static File g(int i4, String str) {
        File file = new File(c(), "user_de");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, String.valueOf(i4));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file2, str);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        return file3;
    }

    public static File h(int i4, String str) {
        File file = new File(c(), "user");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, String.valueOf(i4));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file2, str);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        return file3;
    }

    public static File i(int i4, String str) {
        StringBuilder sb = new StringBuilder();
        String str2 = File.separator;
        sb.append(str2);
        sb.append("data");
        sb.append(str2);
        sb.append("user");
        sb.append(str2);
        sb.append(String.valueOf(i4));
        sb.append(str2);
        sb.append(str);
        return new File(sb.toString());
    }

    public static File j(int i4, String str) {
        File file = new File(k(i4), "/Android/data/" + str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File k(int i4) {
        if (f1181b == null) {
            e.e().m();
            n();
            if (f1181b == null) {
                throw new AndroidRuntimeException("must init sd base dir before.");
            }
        }
        File file = new File(f1181b, "/storage/emulated/");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, String.valueOf(i4));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2;
    }

    public static File l() {
        File file = new File(c(), "system");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File m(int i4) {
        File file = new File(new File(l(), "users"), Integer.toString(i4));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void n() {
        synchronized (a.class) {
            if (f1180a != null) {
                return;
            }
            File file = new File(b.e(), "rootfs");
            f1180a = file;
            if (!file.exists()) {
                file.mkdirs();
            }
            File parentFile = f1180a.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            File file2 = new File(b.f(), "rootfs");
            f1181b = file2;
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
    }
}
