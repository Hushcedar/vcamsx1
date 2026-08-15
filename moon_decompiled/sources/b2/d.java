package b2;

import android.content.ContentProviderClient;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @v1.b(name = "mContentProvider")
    private static v1.c<IInterface> f71a;

    static {
        j.e.q(d.class, ContentProviderClient.class.getName());
    }

    public static IInterface a(ContentProviderClient contentProviderClient) {
        v1.c<IInterface> cVar = f71a;
        if (cVar != null) {
            return cVar.a(contentProviderClient);
        }
        return null;
    }
}
