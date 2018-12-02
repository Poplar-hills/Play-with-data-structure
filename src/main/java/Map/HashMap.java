package Map;

import HashTable.HashTable;

/*
 * - 这里实现的 HashMap 只是实现了 Map 接口的 HashTable（为了性能测试方便），他们底层实现是一样的。而 Java 中的 HashMap 和
 *   Hashtable（注意 t 是小写）是有本质区别的：
 *   1. Hashtable is synchronized, whereas HashMap is not（即 Hashtable 是线程安全的，而 HashMap 不是）. This makes HashMap
 *      is better for non-threaded applications, as unsynchronized Objects typically perform better than synchronized ones.
 *   2. Hashtable does not allow null keys or values, whereas HashMap allows one null key and any number of null values.
 * */

public class HashMap<K, V> implements Map<K,V> {
    HashTable<K, V> map;

    public HashMap() { map = new HashTable<K, V>(); }

    @Override
    public void add(K key, V value) { map.add(key, value); }

    @Override
    public V remove(K key) { return map.remove(key); }

    @Override
    public V get(K key) { return map.get(key); }

    @Override
    public void set(K key, V value) { map.set(key, value); }

    @Override
    public boolean contains(K key) { return map.contains(key); }

    @Override
    public boolean isEmpty() { return map.isEmpty(); }

    @Override
    public int getSize() { return map.getSize(); }
}
