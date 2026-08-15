package t1;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    public class a implements FilenameFilter {
        @Override // java.io.FilenameFilter
        public final boolean accept(File file, String str) {
            return str.endsWith(".apk");
        }
    }

    public class b implements FilenameFilter {
        @Override // java.io.FilenameFilter
        public final boolean accept(File file, String str) {
            return str.endsWith(".apk");
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void b(File file, File file2) throws Throwable {
        File[] fileArrListFiles;
        if (file != null) {
            try {
                if (file2.isDirectory()) {
                    File parentFile = file2.getParentFile();
                    File file3 = new File(parentFile, ".copyTmpDir");
                    File file4 = new File(parentFile, ".targetTmpDir");
                    if (file3.exists()) {
                        e(file3);
                    }
                    if (file4.exists()) {
                        e(file4);
                    }
                    file3.mkdirs();
                    if (file3.exists()) {
                        c(file, file3);
                        fileArrListFiles = file3.listFiles(new a());
                    } else {
                        fileArrListFiles = null;
                    }
                    if (fileArrListFiles != null && fileArrListFiles.length != 0) {
                        if (!file2.renameTo(file4)) {
                            e(file3);
                        } else {
                            if (file3.renameTo(file2)) {
                                e(file4);
                                return;
                            }
                            file4.renameTo(file2);
                        }
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            c(file, file2);
        }
    }

    public static void c(File file, File file2) throws Throwable {
        if (file != null) {
            if (!file.isDirectory()) {
                if (file.isFile()) {
                    if (file2.isDirectory()) {
                        file2 = new File(file2, file.getName());
                    }
                    d(file, file2);
                    return;
                }
                return;
            }
            File[] fileArrListFiles = file.listFiles(new b());
            if (fileArrListFiles != null) {
                for (File file3 : fileArrListFiles) {
                    if (file3.isFile()) {
                        d(file3, new File(file2.isDirectory() ? file2 : file2.getParentFile(), file3.getName()));
                    }
                }
            }
        }
    }

    public static void d(File file, File file2) throws Throwable {
        Throwable th;
        Closeable closeable;
        Closeable closeable2 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                try {
                    FileChannel channel = fileInputStream.getChannel();
                    FileChannel channel2 = fileOutputStream.getChannel();
                    ByteBuffer byteBufferAllocate = ByteBuffer.allocate(1024);
                    while (true) {
                        byteBufferAllocate.clear();
                        if (channel.read(byteBufferAllocate) == -1) {
                            a(fileInputStream);
                            a(fileOutputStream);
                            return;
                        } else {
                            byteBufferAllocate.limit(byteBufferAllocate.position());
                            byteBufferAllocate.position(0);
                            channel2.write(byteBufferAllocate);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    closeable2 = fileOutputStream;
                    closeable = closeable2;
                    closeable2 = fileInputStream;
                    a(closeable2);
                    a(closeable);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            th = th4;
            closeable = null;
        }
    }

    public static int e(File file) {
        boolean z3;
        String[] list;
        int i4 = 0;
        if (file.isDirectory()) {
            try {
                File file2 = file.getParent() == null ? file : new File(file.getParentFile().getCanonicalFile(), file.getName());
                z3 = !file2.getCanonicalFile().equals(file2.getAbsoluteFile());
            } catch (Exception unused) {
                z3 = false;
            }
            if (!z3 && (list = file.list()) != null) {
                int length = list.length;
                int iE = 0;
                while (i4 < length) {
                    iE += e(new File(file, list[i4]));
                    i4++;
                }
                i4 = iE;
            }
        }
        return file.delete() ? i4 + 1 : i4;
    }

    public static void f(String str) {
        e(new File(str));
    }

    public static void g(File file) {
        File[] fileArrListFiles;
        try {
            if (!file.isFile() && (fileArrListFiles = file.listFiles()) != null) {
                for (File file2 : fileArrListFiles) {
                    g(file2);
                }
            }
            file.delete();
        } catch (Exception unused) {
        }
    }

    public static boolean h(String str) {
        String string;
        if (str == null) {
            return false;
        }
        if (TextUtils.isEmpty(str) || ".".equals(str) || "..".equals(str)) {
            string = "(invalid)";
        } else {
            StringBuilder sb = new StringBuilder(str.length());
            for (int i4 = 0; i4 < str.length(); i4++) {
                char cCharAt = str.charAt(i4);
                if (!((cCharAt == 0 || cCharAt == '/') ? false : true)) {
                    cCharAt = '_';
                }
                sb.append(cCharAt);
            }
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            if (bytes.length > 255) {
                while (bytes.length > 252) {
                    sb.deleteCharAt(sb.length() / 2);
                    bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
                }
                sb.insert(sb.length() / 2, "...");
            }
            string = sb.toString();
        }
        return str.equals(string);
    }

    public static boolean i(File file, File file2) {
        if (!file2.isDirectory() || !file.isDirectory()) {
            return false;
        }
        File file3 = new File(file2.getParentFile(), ".targetTmpDir2");
        if (!file2.renameTo(file3)) {
            e(file);
            return false;
        }
        if (file.renameTo(file2)) {
            e(file3);
            return true;
        }
        file3.renameTo(file2);
        return false;
    }
}
