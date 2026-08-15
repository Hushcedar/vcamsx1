package b2;

import android.content.ClipData;
import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mUri")
    private static v1.c<Uri> f70a;

    static {
        j.e.q(c.class, "android.content.ClipData$Item");
    }

    public static void a(ClipData.Item item, Uri uri) {
        v1.c<Uri> cVar = f70a;
        if (cVar != null) {
            cVar.b(item, uri);
        }
    }
}
