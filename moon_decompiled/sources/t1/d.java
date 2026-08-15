package t1;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class d extends Thread {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public FileDescriptor f1561a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final FileDescriptor f1562b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final FileDescriptor f1563c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public volatile boolean f1564d;

    public d() {
        FileDescriptor fileDescriptor = new FileDescriptor();
        this.f1562b = fileDescriptor;
        FileDescriptor fileDescriptor2 = new FileDescriptor();
        this.f1563c = fileDescriptor2;
        try {
            Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_STREAM, 0, fileDescriptor, fileDescriptor2);
        } catch (ErrnoException unused) {
            throw new RuntimeException("Failed to create bridge");
        }
    }

    public static int b(byte[] bArr, int i4, ByteOrder byteOrder) {
        int i5;
        int i6;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            int i7 = i4 + 1;
            int i8 = i7 + 1;
            int i9 = ((bArr[i4] & 255) << 24) | ((bArr[i7] & 255) << 16);
            i5 = i9 | ((bArr[i8] & 255) << 8);
            i6 = (bArr[i8 + 1] & 255) << 0;
        } else {
            int i10 = i4 + 1;
            int i11 = i10 + 1;
            int i12 = ((bArr[i4] & 255) << 0) | ((bArr[i10] & 255) << 8);
            i5 = i12 | ((bArr[i11] & 255) << 16);
            i6 = (bArr[i11 + 1] & 255) << 24;
        }
        return i6 | i5;
    }

    public final void a() {
        try {
            Os.close(this.f1561a);
        } catch (ErrnoException | IOException unused) {
        }
        try {
            Os.close(this.f1562b);
        } catch (ErrnoException | IOException unused2) {
        }
        try {
            Os.close(this.f1563c);
        } catch (ErrnoException | IOException unused3) {
        }
        this.f1564d = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        byte[] bArr = new byte[8192];
        while (true) {
            try {
                try {
                    if (j.e.x(this.f1562b, bArr, 8) != 8) {
                        break;
                    }
                    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
                    int iB = b(bArr, 0, byteOrder);
                    if (iB == 1) {
                        int iB2 = b(bArr, 4, byteOrder);
                        while (iB2 > 0) {
                            int iX = j.e.x(this.f1562b, bArr, Math.min(8192, iB2));
                            if (iX == -1) {
                                throw new IOException("Unexpected EOF; still expected " + iB2 + " bytes");
                            }
                            j.e.D(this.f1561a, bArr, iX);
                            iB2 -= iX;
                        }
                    } else if (iB == 2) {
                        Os.fsync(this.f1561a);
                        j.e.D(this.f1562b, bArr, 8);
                    } else if (iB == 3) {
                        Os.fsync(this.f1561a);
                        Os.close(this.f1561a);
                        this.f1564d = true;
                        j.e.D(this.f1562b, bArr, 8);
                        break;
                    }
                } finally {
                    a();
                }
            } catch (ErrnoException | IOException e4) {
                Log.wtf("FileBridge", "Failed during bridge", e4);
            }
        }
    }
}
