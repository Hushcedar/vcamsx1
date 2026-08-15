package j3;

import android.accounts.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.core.os.perationCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class c implements AutoCloseable {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final String[] f553d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final String[] f554e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final b f555a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Context f556b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final File f557c;

    public static class a extends SQLiteOpenHelper {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int f558a = 0;

        static {
            perationCompat.init0(320);
        }

        public a(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 10);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onCreate(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onOpen(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onUpgrade(SQLiteDatabase sQLiteDatabase, int i4, int i5);
    }

    public static class b extends SQLiteOpenHelper {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f559a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public volatile boolean f560b;

        static {
            perationCompat.init0(319);
        }

        public b(Context context, int i4, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 3);
            this.f559a = i4;
        }

        public final native SQLiteDatabase a();

        public final native SQLiteDatabase b();

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onCreate(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onOpen(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onUpgrade(SQLiteDatabase sQLiteDatabase, int i4, int i5);
    }

    /* JADX INFO: renamed from: j3.c$c, reason: collision with other inner class name */
    public static class C0042c extends SQLiteOpenHelper {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Context f561a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f562b;

        static {
            perationCompat.init0(318);
        }

        public C0042c(int i4, Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 9);
            this.f561a = context;
            this.f562b = i4;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onCreate(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onOpen(SQLiteDatabase sQLiteDatabase);

        @Override // android.database.sqlite.SQLiteOpenHelper
        public final native void onUpgrade(SQLiteDatabase sQLiteDatabase, int i4, int i5);
    }

    static {
        perationCompat.init0(174);
        f553d = new String[]{"type", "authtoken"};
        f554e = new String[]{"key", "value"};
    }

    public c(b bVar, Context context, File file) {
        this.f555a = bVar;
        this.f556b = context;
        this.f557c = file;
    }

    public static native c c(Context context, int i4, File file, File file2);

    public final native void a(File file);

    public final native void b();

    @Override // java.lang.AutoCloseable
    public final native void close();

    public final native void d(long j4);

    public final native boolean e(long j4);

    public final native void f();

    public final native String g(String str, String str2);

    public final native LinkedHashMap h();

    public final native ArrayList i();

    public final native HashMap j();

    public final native HashMap k(Account account);

    public final native long l(Account account);

    public final native long m(Account account);

    public final native String n(Account account);

    public final native long o(long j4, String str);

    public final native LinkedHashMap p();

    public final native HashMap q(Account account);

    public final native long r(Account account, long j4);

    public final native long s(long j4, String str, String str2);

    public final native void t(int i4, String str);

    public final native void u();

    public final native boolean v(Account account);
}
