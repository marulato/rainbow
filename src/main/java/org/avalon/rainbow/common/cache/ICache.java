package org.avalon.rainbow.common.cache;

public interface ICache <K, V> {

    void init();

    V get(K key);

    void update(K key, V value);

    void remove(K key);
}
