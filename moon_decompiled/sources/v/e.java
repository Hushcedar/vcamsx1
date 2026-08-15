package v;

import android.content.UriMatcher;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final UriMatcher f1696a;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        f1696a = uriMatcher;
        uriMatcher.addURI("media", "external/audio/media/#", 1);
    }
}
