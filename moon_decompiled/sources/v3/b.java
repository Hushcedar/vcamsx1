package v3;

import java.util.AbstractSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class b<T> extends AbstractSet<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public a<T> f1722a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final T[] f1723b;

    public static final class a<T> implements Iterator<T> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final T[] f1724a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f1725b;

        public a(T[] tArr) {
            this.f1724a = tArr;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.f1725b != this.f1724a.length;
        }

        @Override // java.util.Iterator
        public final T next() {
            int i4 = this.f1725b;
            this.f1725b = i4 + 1;
            return this.f1724a[i4];
        }

        @Override // java.util.Iterator
        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public b(T[] tArr) {
        this.f1723b = tArr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator<T> iterator() {
        a<T> aVar = this.f1722a;
        if (aVar != null) {
            aVar.f1725b = 0;
            return aVar;
        }
        a<T> aVar2 = new a<>(this.f1723b);
        this.f1722a = aVar2;
        return aVar2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.f1723b.length;
    }
}
