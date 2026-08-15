package v3;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Cloneable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int[] f1726a = b3.a.a(10);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f1727b = 0;

    public final Object clone() {
        c cVar = (c) super.clone();
        cVar.f1726a = (int[]) this.f1726a.clone();
        return cVar;
    }
}
