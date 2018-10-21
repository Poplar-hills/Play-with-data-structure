package Map;

public interface Map<K, V> {
    void add(K key, V value);
    V remove(K key);
    V get(K key);
    void set(K key, V value);
    boolean contains(K key);
    boolean isEmpty();
    int getSize();
}
