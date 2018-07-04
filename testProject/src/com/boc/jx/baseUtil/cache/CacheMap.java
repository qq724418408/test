package com.boc.jx.baseUtil.cache;

import java.util.LinkedHashMap;

/**
 * 缓存容器对象
 * Created by huwentao on 2014/4/30.
 */
public class CacheMap<K, V> extends LinkedHashMap<K, V> {

    public CacheMap(int MAX_SIZE) {
        this.MAX_SIZE = MAX_SIZE;
    }

    public CacheMap(int initialCapacity, int MAX_SIZE) {
        super(initialCapacity);
        this.MAX_SIZE = MAX_SIZE;
    }

    private int MAX_SIZE = 100;

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        if(MAX_SIZE>0) {
            return size() > MAX_SIZE;
        }else{
            return false;
        }
    }
}
