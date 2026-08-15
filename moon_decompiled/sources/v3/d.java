package v3;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    public static boolean a(Intent intent) {
        String schemeSpecificPart;
        Uri data = intent.getData();
        if (data != null && "package".equals(data.getScheme()) && (schemeSpecificPart = data.getSchemeSpecificPart()) != null && i.h.o().d0(schemeSpecificPart, f.e.e().r())) {
            intent.setData(data.buildUpon().encodedOpaquePart(f.e.e().n()).build());
        }
        int iR = f.e.e().r();
        String strN = f.e.e().n();
        Uri uriA = j.k.a(intent, strN, iR);
        Uri uriB = j.k.b(strN, intent.getData(), iR);
        if (uriB != null) {
            intent.setDataAndType(uriB, intent.getType());
        }
        return (uriB == null && uriA == null) ? false : true;
    }

    public static String b(Intent intent) {
        if (intent == null) {
            return null;
        }
        ComponentName component = intent.getComponent();
        return component != null ? component.getPackageName() : intent.getPackage();
    }

    public static boolean c(Intent intent) {
        ComponentName component = intent.getComponent();
        return component != null && component.getClassName().startsWith("com.hack.agent.HackAppActivity");
    }

    public static boolean d(Intent intent) {
        return intent != null && TextUtils.equals(intent.getAction(), "android.intent.action.MAIN") && (intent.hasCategory("android.intent.category.LAUNCHER") || intent.hasCategory("android.intent.category.INFO"));
    }
}
