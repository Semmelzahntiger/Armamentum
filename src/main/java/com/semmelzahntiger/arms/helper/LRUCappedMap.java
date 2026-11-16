package com.semmelzahntiger.arms.helper;

import java.util.LinkedHashMap;

public class LRUCappedMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;
    public LRUCappedMap(int maxSize) {
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
