package com.semmelzahntiger.arms.core.simulate;

import java.util.LinkedHashMap;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;
    public LRUMap(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
    public int getMaxSize() {
        return maxSize;
    }
}
