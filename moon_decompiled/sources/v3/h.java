package v3;

import android.os.Parcel;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public abstract class h {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final char[] f1729d = {'h', 'a', 'c', 'k'};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final File f1730a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final File f1731b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Object f1732c = new Object();

    public h(File file) {
        this.f1730a = file;
        this.f1731b = new File(file.getParent(), file.getName() + ".tmp");
    }

    public static void b(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
    }

    public int c() {
        return 0;
    }

    public final void d() throws Throwable {
        FileInputStream fileInputStream;
        File file = this.f1730a;
        Parcel parcelObtain = Parcel.obtain();
        FileInputStream fileInputStream2 = null;
        try {
            try {
                if (!file.exists()) {
                    Objects.toString(file);
                    parcelObtain.recycle();
                    return;
                }
                int length = (int) file.length();
                byte[] bArr = new byte[length];
                fileInputStream = new FileInputStream(file);
                try {
                    if (length != fileInputStream.read(bArr)) {
                        Objects.toString(file);
                        parcelObtain.recycle();
                        b(fileInputStream);
                        return;
                    }
                    parcelObtain.unmarshall(bArr, 0, length);
                    parcelObtain.setDataPosition(0);
                    if (!Arrays.equals(parcelObtain.createCharArray(), f1729d)) {
                        Objects.toString(file);
                    }
                    e(parcelObtain, parcelObtain.readInt());
                    parcelObtain.recycle();
                    b(fileInputStream);
                } catch (Exception e4) {
                    e = e4;
                    fileInputStream2 = fileInputStream;
                    e.printStackTrace();
                    parcelObtain.recycle();
                    b(fileInputStream2);
                } catch (Throwable th) {
                    th = th;
                    parcelObtain.recycle();
                    b(fileInputStream);
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
        }
    }

    public abstract void e(Parcel parcel, int i4);

    public final void f() {
        byte[] bArrMarshall;
        FileOutputStream fileOutputStream;
        boolean zRenameTo;
        Parcel parcelObtain = Parcel.obtain();
        try {
            try {
                parcelObtain.writeCharArray(f1729d);
                parcelObtain.writeInt(c());
                g(parcelObtain);
                bArrMarshall = parcelObtain.marshall();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            synchronized (this.f1732c) {
                File file = this.f1731b;
                if (file != null) {
                    FileOutputStream fileOutputStream2 = null;
                    try {
                        try {
                            fileOutputStream = new FileOutputStream(file);
                        } catch (Exception e5) {
                            e = e5;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                    }
                    try {
                        fileOutputStream.write(bArrMarshall);
                        fileOutputStream.getFD().sync();
                        zRenameTo = file.renameTo(this.f1730a);
                        b(fileOutputStream);
                    } catch (Exception e6) {
                        e = e6;
                        fileOutputStream2 = fileOutputStream;
                        e.printStackTrace();
                        b(fileOutputStream2);
                        zRenameTo = false;
                    } catch (Throwable th2) {
                        th = th2;
                        b(fileOutputStream);
                        throw th;
                    }
                    if (!zRenameTo) {
                        h(bArrMarshall);
                    }
                } else {
                    h(bArrMarshall);
                }
            }
        } finally {
            parcelObtain.recycle();
        }
    }

    public abstract void g(Parcel parcel);

    public final void h(byte[] bArr) throws Throwable {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(this.f1730a);
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Throwable th) {
            th = th;
            fileOutputStream = fileOutputStream2;
        }
        try {
            fileOutputStream.write(bArr);
            fileOutputStream.getFD().sync();
            b(fileOutputStream);
        } catch (Exception e5) {
            e = e5;
            fileOutputStream2 = fileOutputStream;
            e.printStackTrace();
            b(fileOutputStream2);
        } catch (Throwable th2) {
            th = th2;
            b(fileOutputStream);
            throw th;
        }
    }
}
