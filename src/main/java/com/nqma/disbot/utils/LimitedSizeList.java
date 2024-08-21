package com.nqma.disbot.utils;

import java.util.ArrayList;

public class LimitedSizeList<E> extends ArrayList<E> {
    private final int maxSize;

    public LimitedSizeList(int maxSize) {
        super(maxSize);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (this.size() >= maxSize) {
            this.remove(0);
        }
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        if (this.size() >= maxSize) {
            this.remove(0);
        }
        super.add(index, element);
    }
}
