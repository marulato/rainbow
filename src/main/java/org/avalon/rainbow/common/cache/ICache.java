package org.avalon.rainbow.common.cache;

public interface ICache <K, V> {

    V get(K key);

    void put(K key, V value);

    void update(K key, V value);

    void remove(K key);
}
