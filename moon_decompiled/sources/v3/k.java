package v3;

/* JADX INFO: loaded from: classes.dex */
public abstract class k<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private T f1736a;

    public abstract T a();

    public final T b() {
        T t4;
        synchronized (this) {
            if (this.f1736a == null) {
                this.f1736a = a();
            }
            t4 = this.f1736a;
        }
        return t4;
    }
}
