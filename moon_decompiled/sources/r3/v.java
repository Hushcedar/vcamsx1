package r3;

import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HashMap<String, Integer> f1445a = new HashMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String[] f1446b = {"android.permission.ACCOUNT_MANAGER", "android.permission.GET_ACCOUNTS", "android.permission.GET_ACCOUNTS_PRIVILEGED", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final String[] f1447c = {"android.permission.MANAGE_ACCOUNTS", null, null, null, null};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final boolean[] f1448d = {true, true, true, false, false};

    static {
        int i4 = 0;
        while (true) {
            String[] strArr = f1446b;
            if (i4 >= strArr.length) {
                return;
            }
            f1445a.put(strArr[i4], Integer.valueOf(i4));
            i4++;
        }
    }
}
