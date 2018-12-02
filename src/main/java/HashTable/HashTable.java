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
 *     位置上的链表转换成红黑树（即 TreeMap）。这是因为在哈希冲突较少时（如3以内）链表就够快了，而且开销比红黑树低（不需要各种旋转）。
 *   - 这里我们实现的 hashTable 只采用 TreeMap 以保持简单。
 *   - 在哈希冲突过多的原因：1.哈希表设计有问题（如开辟的数组太小，即 M 太小） 2.数据有问题（如全是一个数）。对于第一个问题，当要存
 *     入 hashTable 的数据集的大小是动态的时候，M 的取值太小也应该是动态的，即开辟的数组大小应该能动态 resize。
 *     - resize 的条件：
 *       1. 当数组中每个位置平均承载的元素个数多过一定程度时扩容：n / M >= upperTol（容忍度上线）
 *       2. 当数组中每个位置平均承载的元素个数少过一定程度时缩容：n / M <= lowerTol（容忍度下线）
 *     - resize 的原理：和之前实现的数组是一致的，即把现有的 hashTable 中的数据搬到新创建的扩容或缩容之后的 hashTable 中。
 *   - 像数组的 resize 一样，hashTable 的时间复杂度也是可以均摊到 add 或 remove 操作中计算的，即当元素数从 n 增加到 n * upperTol
 *     （即数组的每个位置上存储的元素个数都达到上限） 时，数组会被 resize，从而使得地址空间倍增。其中：
 *       1. 元素数从 n 增加到 n * upperTol，增加了 n * upperTol - n 个元素，即运行了 n * upperTol - n 多次 add/remove 操作。
 *       2. resize 地址空间倍增的时间复杂度是 O(n) 级别的（比如 M * 2）
 *     因此，将 resize 的复杂度平摊到每一次 add/remove 操作上：2M / (n * upperTol - n) < 1。因此 add/remove 操作加上 resize
 *     的总时间复杂度是小于 O(2) 的，因此还是 O(1) 级别的。
 *   - 之前在讲数组动态扩容的时候我们是简单的把容量乘以2，而 hashTable 的扩容不太一样，因为我们希望 hashTable 的数组容量 M 是一个
 *     素数，从而使哈希值分布地更均匀，因此直接扩容到 2M 会破坏这一点。更好的做法是建立一个素数数组，每次扩容的时候从该素数数组中取出
 *     下一个素数作为新的数组容量。
 *
 *  - 哈希表与平衡树的比较：
 *    1. 哈希表的复杂度比平衡树低：哈希表为 O(1)；平衡树为 O(logn)
 *    2. 但有得必有失，哈希表牺牲了顺序性；而平衡树维护了所存数据的顺序性（可以快速获得最大值、最小值、第 k 个值、某个元素的 rank、
 *       predecessor、successor 等）。
 *    - Set, Map 这两种抽象数据结构底层实现可以是链表、树、哈希表。但根据是否保留数据的顺序性，我们可以将它们分类如下：
 *      1. 有序集合、有序映射：底层是平衡树（在 java 中对应的是 TreeSet, TreeMap）
 *      2. 无序集合、无序映射：底层是哈希表（在 java 中对应的是 HashSet, HashMap）
 * */

public class HashTable<K, V> {  // K 不再需要 extends Comparable，因为比较的方式变成了比哈希值，因此只需要实现 hashCode 方法即可，而 hashCode 方法是定义在 Object 上的，所有变量都有。
    private static final int upperTol = 10;  // 数组中每个位置平均最多存储10个元素，>= 10 的话就要扩容
    private static final int lowerTol = 2;   // <= 2 的话就要缩容
    private final int[] capacity = { 53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157, 98317,
            196613, 393241, 786433, 1572869, 3145739, 6291469, 12582917, 25165843, 50331653, 100663319,
            201326611, 402653189, 805306457, 1610612741};
    private int capacityIndex = 0;   // 数组的初始容量索引

    private TreeMap<K, V>[] hashTable;  // 哈希表实际上是一个 TreeMap 数组
    private int M;     // 开辟的数组大小
    private int size;  // 元素个数，即 n

    public HashTable() {
        this.M = capacity[capacityIndex];
        size = 0;
        hashTable = new TreeMap[M];
        for (int i = 0; i < M; i++)
            hashTable[i] = new TreeMap<K, V>();
    }

    /*
    * 辅助方法
    * */
    private void resize(int newM) {
        // 开辟新数组的空间
        TreeMap<K, V>[] newHashTable = new TreeMap[newM];

        // 初始化新数组
        for (int i = 0; i < newM; i++)
            newHashTable[i] = new TreeMap<>();

        // 将旧 hashTable 中的每一颗 TreeMap 上的每一个键值对拿出来，然后重新 hash 并放入新 hashTable 里
        int oldM = this.M;
        this.M = newM;  // 因为下面用到 hash 方法，而 hash 方法内部用到的 M 应该是 newM，因此这里要更新才行
        for (int i = 0; i < oldM; i++) {  // 而这行的 M 又需要的是旧的 M，因此要在上面保存一下就的 M 给这里用
            TreeMap<K, V> map = hashTable[i];
            for (K key : map.keySet())
                newHashTable[hash(key)].put(key, map.get(key));
        }

        // 更新
        this.hashTable = newHashTable;
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
        else {  // 不存在则添加
            map.put(key, value);
            size++;

            if (size >= upperTol * M && capacityIndex + 1 < capacity.length)  // 小技巧，相当于 if ((float) size / M >= upperTol)。因为2个 int 相除会丢掉小数位，想保留的话就要转成 float。
                resize(capacity[++capacityIndex]);
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

            if (size <= lowerTol * M && capacityIndex - 1 >= 0)  // 注意除法分母不能为零，这里我们让它不小于 initCapacity
                resize(capacity[--capacityIndex]);  // 缩容
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
