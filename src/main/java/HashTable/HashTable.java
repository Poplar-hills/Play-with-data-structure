package HashTable;

import java.util.TreeMap;

/*
 * - 什么是哈希表：把我们关心的数据通过某种方法（哈希函数）转化成一个索引，然后在一个数组的该索引位置存储这些数据。因为数组支持随机访问
 *   的特性，因此我们可以以 O(1) 的复杂度快速查找到这些数据。
 * - 哈希表的核心思想：用空间换时间。但是因为任意设备都不可能有无限的空间，因此哈希表的设计就是要平衡空间和时间。
 * - 两个核心问题：
 *   1. 哈希函数的设计：如何将一份数据（可能是任意类型）转化成一个尽可能唯一的索引？
 *   2. 解决哈希冲突：当两份不同的数据通过哈希函数转化成了同一个索引（哈希冲突）时，如何能解决这种哈希冲突？
 *
 * - 哈希函数的设计原则：
 *   1. 一致性：如果 a == b 则 hashCode(a) == hashCode(b)，但反之不一定成立（因为可能有哈希冲突）
 *   2. 计算高效简便
 *   3. 数据通过哈希函数计算出来的索引的分布约均匀越好。
 * - 哈希函数的设计思路：
 *   - 特殊的领域有特殊的设计方法。
 *   - 比较常见的普适性设计是将数据转换成整数（不管数据是什么类型都可以通过某种方式转换成整数）在计算该整数的哈希值：
 *     1. 对于小范围的正整数直接使用
 *     2. 对于小范围的负整数进行便宜：-100 ~ 100 --> 0 ~ 200
 *     3. 对于大整数进行取模：对身份证号 110101198702020523 mod 1000000，即保留后6位，得到 020523。但这样得到的哈希值分布不均
 *        匀，更好的做法：模一个素数。具体模多大的素数有一个专门表格可以参考（略）。
 *     4. 浮点型：浮点数在计算机中都是32位或64位的二进制存储，只不过被计算机解析成浮点型，因此仍然可以转换成整型处理。
 *     5. 字符串：转换成整型处理。
 *     6. 复合类：本 package 中实现的 Student 类来说，它由4个成员变量组成，因此可以看做是一个类似 Date 的复合类型，其哈希函数的设
 *        计只需要计算出每个成员变量的 hash code，然后通过上面的公式计算即可。
 *     - Java 中的基本类型都已经有定义好的哈希函数，只需调用 hashCode 方法即可，对于复合类型需要自己定义哈希函数。
 *
 * - 哈希冲突的处理方法 —— 链地址法（Separating Chaining）
 *   - 哈希表的本质就是一个数组，将数据通过哈希函数映射成不同的索引，然后再把数据存储到数组中该索引的位置上。
 *   - 前面说过，对于大整数计算哈希值的时候要模一个素数，这个素数 M 其实就是在实现哈希表时要开辟的数组的大小（因为对一系列很大的数字
 *     取模实际上就是把这些大数映射到一个小范围里的过程，如 123456、234567、345678 mod 31 实际上是把这三个数映射到31以内）。因此
 *     开辟的空间越大，哈希冲突的几率越小，实现出来的 hashTable 就越快（空间换时间）。
 *   - 因为开辟的空间总是有限的，因此哈希冲突在所难，当发生冲突时（即两个数据计算出了同样的哈希值，要存储到数组中同一个位置上），此时：
 *
 *     | 0 | 1 | 2 | 3 | 4 | ... | M-1 |
 *          k1          k2
 *          k3
 *
 *     此时 k1、k3 都存储在索引为1的位置上，因此我们可以将它做成链表。这样整个 hashTable 实际上就是一个 LinkedList 数组，这也是
 *     Separate Chaining 的含义所在。
 *   - 当哈希冲突时，我们需要的实际上不一定是"链表"，而只要是一种查找表就可以，因此可以是链表，也可以是树结构。这样整个 hashTable
 *     实际上就是一个 TreeMap 数组（如果要实现的是 HashSet 则是一个 TreeSet 数组）。
 *   - 在 Java8 之前，每个位置对应的就是一个链表；从 Java8 开始，每个位置对应的先是一个链表，当哈希冲突到达一定程度时，会自动将每个
 *     位置上的链表转换成红黑树（即 TreeMap）。这是因为在哈希冲突较少时（如5以内）链表就够快了，而且开销比红黑树低（不需要各种旋转）。
 *   - 这里我们实现的 hashTable 只采用 TreeMap 以保持简单。
 *
 * - hashCode 方法只用于计算哈希值，但当产生哈希冲突的时候（两个不同对象计算出了相同的哈希值），仍然需要比较两个对象是否相等。比如，
 *   如果要使用 Student 类实例作为 HashSet 的元素或 HashMap 的 key 的话d，光有 hashCode 方法还不够，还需要 equals 方法在产出哈希
 *   冲突的时候比较两个对象是否相同。
 * */

public class HashTable<K, V> {  // K 不再需要 extends Comparable，因为比较的方式变成了比哈希值，因此只需要实现 hashCode 方法即可，而 hashCode 方法是定义在 Object 上的，所有变量都有。
    private TreeMap<K, V>[] hashTable;  // 哈希表实际上是一个 TreeMap 数组
    private int M;  //
    private int size;

    public HashTable(int M) {
        this.M = M;
        size = 0;
        hashTable = new TreeMap[M];
        for (int i = 0; i < M; i++)
            hashTable[i] = new TreeMap<K, V>();
    }

    public HashTable() {
        this(97);
    }

    // 此处的哈希函数做了2件事：
    // 1. 调用 key 上的 hashCode 方法，将可能为任意类型的 key 转化为整型
    // 2. 将得到的整型通过位与操作消除符号（相当于取绝对值），再模上 M，最终得到我们需要的哈希值
    private int hash(K key) {
        return (key.hashCode() & 0x7ffffff) % M;
    }

    /*
    * 增操作
    * */
    public void add(K key, V value) {
        TreeMap<K, V> map = hashTable[hash(key)];
        if (map.containsKey(key))  // 如果已经存在，则做更新操作（相当于 set）
            map.put(key, value);
        else {
            map.put(key, value);
            size++;
        }
    }

    /*
     * 删操作
     * */
    public V remove(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        V ret = null;
        if (map.containsKey(key)) {
            ret = map.remove(key);
            size--;
        }
        return ret;
    }

    /*
     * 改操作
     * */
    public void set(K key, V value) {
        TreeMap<K, V> map = hashTable[hash(key)];
        if (!map.containsKey(key))
            throw new IllegalArgumentException("set failed. key doesn't exist in the hashtable");
        map.put(key, value);
    }

    /*
     * 查操作
     * */
    public int getSize() { return size; }

    public boolean contains(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        return map.containsKey(key);
    }

    public V get(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        return map.get(key);
    }

    public Boolean isEmpty() { return size == 0; }
}
