package com.j256.ormlite.dao;

import com.j256.ormlite.misc.IOUtils;
import java.io.IOException;

public class CloseableWrappedIterableImpl<T> implements CloseableWrappedIterable<T> {
    private final CloseableIterable<T> iterable;
    private CloseableIterator<T> iterator;

    public CloseableWrappedIterableImpl(CloseableIterable<T> iterable) {
        this.iterable = iterable;
    }

    public CloseableIterator<T> iterator() {
        return closeableIterator();
    }

    public CloseableIterator<T> closeableIterator() {
        IOUtils.closeQuietly(this);
        this.iterator = this.iterable.closeableIterator();
        return this.iterator;
    }

    public void close() throws IOException {
        if (this.iterator != null) {
            this.iterator.close();
            this.iterator = null;
        }
    }
}
