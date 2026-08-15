package com.core;

import a.f;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import b.c;
import b.d;
import f.e;
import java.lang.reflect.InvocationHandler;
import n3.b;
import p3.a;

/* JADX INFO: loaded from: classes.dex */
public class Cmd {
    public static Object exec(int i4, Object... objArr) {
        if (i4 == 1) {
            f.e().a((Application) objArr[0], (Context) objArr[1]);
            return null;
        }
        if (i4 == 2) {
            f.e().y();
            return null;
        }
        if (i4 == 90) {
            return a.a((Intent) objArr[0]);
        }
        if (i4 == 91) {
            return Boolean.valueOf(a.b((Intent) objArr[0]));
        }
        if (i4 == 150) {
            return Boolean.valueOf(b.b((Context) objArr[0]));
        }
        if (i4 == 151) {
            return b.a((String) objArr[0], (String) objArr[1], (Bundle) objArr[2]);
        }
        if (i4 == 160) {
            return f.e().g();
        }
        if (i4 == 161) {
            return f.e().h((String) objArr[0]);
        }
        if (i4 == 220) {
            return n3.a.a().b((Activity) objArr[0]);
        }
        if (i4 == 221) {
            return n3.a.a().c((Activity) objArr[0]);
        }
        switch (i4) {
            case 9:
                return Boolean.valueOf(f.e().v(((Integer) objArr[0]).intValue(), (String) objArr[1]));
            case 10:
                int iIntValue = ((Integer) objArr[0]).intValue();
                int iIntValue2 = ((Integer) objArr[2]).intValue();
                Object obj = objArr[1];
                if (obj instanceof ParcelFileDescriptor[]) {
                    return Integer.valueOf(f.e().u(iIntValue, null, iIntValue2, (ParcelFileDescriptor[]) obj));
                }
                return Integer.valueOf(f.e().u(iIntValue, (String) obj, iIntValue2, null));
            case 11:
                return Integer.valueOf(f.e().J(((Integer) objArr[0]).intValue(), (String) objArr[1], ((Integer) objArr[2]).intValue()));
            default:
                switch (i4) {
                    case 13:
                        return Integer.valueOf(f.e().d(((Integer) objArr[0]).intValue(), (String) objArr[1], ((Integer) objArr[2]).intValue()));
                    case 14:
                        return Integer.valueOf(f.e().c(((Integer) objArr[0]).intValue(), (String) objArr[1], ((Integer) objArr[2]).intValue()));
                    case 15:
                        return Boolean.valueOf(f.e().w(((Integer) objArr[0]).intValue(), (String) objArr[1], (String) objArr[2]));
                    case 16:
                        return Boolean.valueOf(f.e().t(((Integer) objArr[0]).intValue(), (String) objArr[1]));
                    case 17:
                        return f.e().p(((Integer) objArr[0]).intValue());
                    default:
                        switch (i4) {
                            case 20:
                                return f.e().m(((Integer) objArr[0]).intValue(), (String) objArr[1], ((Integer) objArr[2]).intValue());
                            case 21:
                                return f.e().F((Intent) objArr[0], (String) objArr[1], ((Integer) objArr[2]).intValue(), ((Integer) objArr[3]).intValue());
                            case 22:
                                return f.e().z((Intent) objArr[0], (String) objArr[1], ((Integer) objArr[2]).intValue(), ((Integer) objArr[3]).intValue());
                            case 23:
                                return f.e().f((ComponentName) objArr[0], ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue());
                            case 24:
                                return f.e().k(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue());
                            case 25:
                                return f.e().n((String) objArr[0], ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue());
                            case 26:
                                return f.e().l((String) objArr[0], ((Integer) objArr[1]).intValue());
                            case 27:
                                return f.e().r(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue());
                            case 28:
                                break;
                            case 100:
                                return c.a((String) objArr[0], (String) objArr[1], (Bundle) objArr[2]);
                            case 120:
                                b.b.a((Context) objArr[0], (Intent) objArr[1]);
                                return null;
                            case 210:
                                return n3.a.a().g((String) objArr[0], (String) objArr[1], (Bundle) objArr[2]);
                            case 240:
                                return d.a().b((Uri) objArr[0]);
                            case 300:
                                return e.e().q();
                            case 400:
                                f.e().B((InvocationHandler) objArr[0]);
                                break;
                            case 500:
                                return f.e().s(objArr);
                            case 200000:
                                return f.e().x();
                            case 200001:
                                return Integer.valueOf(f.e().b());
                            default:
                                switch (i4) {
                                    case 30:
                                        return Boolean.valueOf(f.e().I((String) objArr[0], ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue()));
                                    case 31:
                                        return Integer.valueOf(f.e().H((Intent) objArr[0], (Bundle) objArr[1], ((Integer) objArr[2]).intValue()));
                                    case 32:
                                        return Integer.valueOf(f.e().A((Activity) objArr[0], (Intent) objArr[1], ((Integer) objArr[2]).intValue()));
                                    case 33:
                                        return Boolean.valueOf(f.e().M((String) objArr[0], ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue()));
                                    case 34:
                                        return f.e().o(((Integer) objArr[0]).intValue());
                                    default:
                                        switch (i4) {
                                            case 40:
                                                return f.e().C(objArr[0]);
                                            case 41:
                                                f.e().K(objArr[0]);
                                                return null;
                                            case 42:
                                                return f.e().D(objArr[0]);
                                            case 43:
                                                f.e().L(objArr[0]);
                                                return null;
                                            case 44:
                                                return Boolean.valueOf(f.e().G(((Integer) objArr[0]).intValue(), (String) objArr[1]));
                                            case 45:
                                                return f.e().i(((Integer) objArr[0]).intValue(), (String) objArr[1]);
                                            default:
                                                return null;
                                        }
                                }
                        }
                        return Boolean.valueOf(f.e().N((String) objArr[0], ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue(), ((Boolean) objArr[3]).booleanValue()));
                }
        }
    }
}
