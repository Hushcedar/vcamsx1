package k2;

import android.os.Build;
import android.os.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.e(name = "setPermissions", value = {File.class, int.class, int.class, int.class})
    private static v1.h<Integer> f690a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @v1.e(name = "copyFileOrThrow", value = {File.class, File.class})
    private static v1.h<Void> f691b;

    static {
        j.e.q(d.class, "android.os.FileUtils");
    }

    public static void a(FileInputStream fileInputStream) {
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void b(File file, File file2) throws IOException {
        if (Build.VERSION.SDK_INT < 29) {
            v1.h<Void> hVar = f691b;
            if (hVar != null) {
                hVar.a(new Object[]{file, file2});
                return;
            }
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                try {
                    FileUtils.copy(fileInputStream, fileOutputStream);
                    fileOutputStream.close();
                    fileInputStream.close();
                } finally {
                }
            } finally {
            }
        } catch (IOException e4) {
            throw e4;
        }
    }

    public static void c(File file) {
        f690a.a(new Object[]{file, 505, -1, -1}).intValue();
    }
}
