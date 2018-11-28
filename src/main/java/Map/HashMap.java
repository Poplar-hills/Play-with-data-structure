package Map;

import HashTable.HashTable;

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
