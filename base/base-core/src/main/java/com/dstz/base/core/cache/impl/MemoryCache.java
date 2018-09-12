package com.dstz.base.core.cache.impl;

import com.dstz.base.core.cache.ICache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存的cache实现。
 */
public class MemoryCache<T> implements ICache<T> {

    private Map<String, T> map = new ConcurrentHashMap<String, T>();

    public void add(String key, T obj) {
        map.put(key, obj);

    }

    public void delByKey(String key) {
        map.remove(key);
    }

    public void clearAll() {
        map.clear();
    }

    public T getByKey(String key) {
        return map.get(key);
    }

    public boolean containKey(String key) {

        return map.containsKey(key);
    }

    public void add(String key, T obj, int timeout) {
    	
    }

}
