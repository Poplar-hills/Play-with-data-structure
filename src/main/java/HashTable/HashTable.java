package HashTable;

import java.util.TreeMap;

/*
 * - 什么是哈希表：
 *   - 哈希表的本质就是一个数组，将数据通过哈希函数映射成不同的索引，然后再把数据存储到数组中该索引的位置上。
 *   - 因为数组支持随机访问的特性，因此查找到这些数据的复杂度是 O(1) 。
 *
 * - 哈希表的两个核心问题：
 *   1. 哈希函数的设计：如何将数据（可能是任意类型）中的任意元素转化成尽可能唯一的索引？
 *   2. 解决哈希冲突：当两份不同的数据通过哈希函数转化成了同一个索引（哈希冲突）时，如何解决？
 *
 * - 哈希表的核心思想：
 *   - 用空间换时间。但是因为任意设备都不可能有无限的空间，因此哈希表的设计就是要平衡空间和时间。给哈希表开多大的空间对
 *     于其性能有很大影响，直接决定了它可能会有多少哈希冲突。
 *   - 如果我们能开辟无限长的数组，则完全可以以身份证号作为索引把14亿居民的信息都存入数组。虽然会浪费大量空间，但是完全
 *     没有哈希冲突，增删改查都能在 O(1) 的时间内完成（给定一个人的身份证号，找到这个人的信息）。
 *   - 另一个极端，如果我们只能开辟长度为1的数组，则所有数据就都得挤在这一个空间中。这就相当于所有数据都产生了哈希冲突，
 *     都映射到了同一个索引上，此时数据无法通过随机访问查询，因此此时的增删改查就都是 O(n) 的复杂度（给定一个身份证号，
 *     需要遍历所有人才能找到这个人的信息）。
 *
 * - 哈希函数的设计原则：
 *   1. 一致性：如果 a == b 则 hashCode(a) == hashCode(b)，但反之不一定成立（因为可能有哈希冲突）
 *   2. 高效性：计算高效简便
 *   3. 均匀性：哈希函数计算出来的索引的分布约均匀越好
 *
 * - 哈希函数的设计思路：
 *   - 不同的领域有都其对应的设计方法。
 *   - 比较常见的普适性设计是将数据转换成整数（不管数据是什么类型都可以通过某种方式转换成整数），再计算该整数的哈希值：
 *     1. 对于小范围的正整数直接使用；
 *     2. 对于小范围的负整数进行偏移：-100 ~ 100 --> 0 ~ 200；
 *     3. 对于大整数进行取模：对身份证号 110101198702020523 mod 1000000，即保留后6位，得到 020523。但这样得到的哈希值分布不均
 *        匀，更好的做法：模一个素数。具体模多大的素数有一个专门表格可以参考（略）。
 *     4. 浮点型：浮点数在计算机中都是32位或64位的二进制存储，只不过被计算机解析成浮点型，因此仍然可以转换成整型处理。
 *     5. 字符串：转换成整型处理（code = c * 26^3 + o * 26^2 + d * 26^1 + e * 26^0）。
 *     6. 复合类：本 package 中实现的 Student 类来说，它由4个成员变量组成，因此可以看做是一个类似 Date 的复合类型，其哈希函数的设
 *        计只需要计算出每个成员变量的 hash code，然后通过上面的公式计算即可。
 *
 * - Java 中的基本类型都已经有定义好的哈希函数（Object 上的 hashCode() 方法），可以将任意数据类型对应到一个整型上。对于复合类型需
 *   要自己定义哈希函数（将 Student.java）。但哈希函数只用于计算哈希值，但当产生哈希冲突的时候，需要一个方法比较两个对象是否相等，
 *   这就需要定义 equals 方法。
 *
 * - 哈希冲突的处理方法 —— 链地址法（Separating Chaining）
 *   - 前面说过，对于大整数计算哈希值的时候要模一个素数，这个素数 M 其实就是在实现哈希表时要开辟的数组的大小（因为对一系列很大的数字
 *     取模实际上就是把这些大数映射到一个小范围里的过程，如 123456、234567、345678 mod 31 实际上是把这三个数映射到31以内）。因此
 *     开辟的空间越大，哈希冲突的几率越小，实现出来的 hashTable 就越快（空间换时间）。
 *   - 因为开辟的空间总是有限的，因此哈希冲突在所难，当发生冲突时：
 *     | 0 | 1 | 2 | 3 | 4 | ... | M-1 |
 *          k1          k2
 *          k3
 *     将 k1、k3 以链表的结构存储在1的位置上，让整个 hashTable 变成一个 LinkedList 数组，这就是 Separate Chaining。
 *   - 实际上不一定是"链表"，而只要是一种查找表就可以，因此还可以是 TreeMap 结构。这样整个 hashTable 变成了一个 TreeMap 数组
 *     （如果要实现的是 HashSet 则是一个 TreeSet 数组）。
 *     - 在 Java8 之前，HashMap 的实现中每个位置对应的就是一个链表；
 *     - 从 Java8 开始，每个位置对应的先是一个链表，当哈希冲突到达一定程度时，会自动将每个位置上的链表转换成红黑树（即 TreeMap）。
 *       这是因为在哈希冲突较少时（比如3以内）链表的开销比红黑树低（不需要各种旋转），而且足够快。
 *   - 这里我们实现的 hashTable 只采用 TreeMap 以保持简单。
 *   - Java 标准库中哈希表的底层处理哈希冲突的方式也是 Separate Chaining。但除此之外，还有 Open Addressing、Rehashing、
 *     Coalesced Hashing 等其他方法来处理哈希冲突。
 *
 * - 哈希表的复杂度分析：
 *   若给哈希表开辟 M 大小的空间，存入 N 个元素，则平均每个地址上有 N/M 个元素（哈希冲突）：
 *   - 若采用链表实现 separate chaining，则平均复杂度为 O(N/M)
 *   - 若采用 TreeMap 实现 separate chaining，则平均复杂度为 O(log(N/M))
 *   也就是说：
 *   1. 当 M 是固定值时，哈希表的复杂度 = 用于实现 separate chaining 的查找表的复杂度。
 *   2. 当哈希冲突处于平均水平时，复杂度是 O(N/M) 或 O(log(N/M))；而当哈希冲突在最坏情况下（所有元素都映射到了同一个索引上，即
 *     全部都哈希冲突了），复杂度就成了 O(N) 或 O(logN)。
 *
 * - 在哈希冲突过多的原因：
 *   1. 哈希表设计有问题（如开辟的数组空间 M 太小，或哈希分布严重不均）
 *   2. 数据有问题（如全是一个数，正常情况下不会，但有例外，如哈希碰撞攻击）
 *
 * - 哈希冲突过多的解决方案：
 *   哈希冲突的第二个原因是哈希表无法控制的，但可以对第一个原因进行优化：当要存入哈希表的数据集的大小是动态的时候，M 的取值太小也应该
 *   是动态的，即开辟的数组大小应该能动态 resize。
 *   - resize 的条件：当数组中每个位置平均承载的元素个数：
 *     1. 多过一定程度时，进行扩容：N / M >= upperTol（容忍度上线）
 *     2. 少过一定程度时，进行缩容：N / M <= lowerTol（容忍度下线）
 *   - resize 的原理：和之前实现的数组是一致的，即把当前哈希表中的数据搬到新创建的扩容/缩容之后的哈希表中，其中"搬"的过程就是重新映射的过程。
 *   - 像数组的 resize 一样，哈希表 resize 的时间复杂度也可以均摊到 add 或 remove 操作中，即当元素数从 N 增加到 N * upperTol
 *     （即数组的每个位置上存储的元素个数都达到上限）时，数组会被 resize，从而使得地址空间倍增。在扩容过程中：
 *       1. 元素数从 N 增加到 N * upperTol，增加了 N * upperTol - N 个元素，即该过程中运行了 N * upperTol - N 次 add 操作。
 *       2. resize 方法本身的时间复杂度是 O(N) 级别的（因为要遍历所有元素）
 *     因此，将 resize 的复杂度平摊到每一次 add 操作上：N / (N * upperTol - N) < 1。因此 add 操作加上 resize 操作的总时间复杂
 *     度是小于 O(2) 的，因此还是 O(1) 级别的。
 *   - 关于扩容倍数：之前在讲数组动态扩容时，是简单的把容量乘以2，而哈希表的扩容不太一样。因为我们希望哈希表的容量 M 是一个素数，从而
 *     使哈希值分布地更均匀，因此直接扩容到 2M 会破坏这一点。更好的做法是建立一个素数数组，每次扩容的时候从该素数数组中取出下一个素数
 *     作为新的数组容量。
 *
 *  - 哈希表与平衡树的比较：
 *    1. 哈希表的复杂度比平衡树低：哈希表为 O(1)；平衡树为 O(logn)
 *    2. 但有得必有失，哈希表牺牲了顺序性；而平衡树维护了所存数据的顺序性（可以快速获得最大值、最小值、第 k 个值、某个元素的 rank、
 *       predecessor、successor 等）。
 *    - Set, Map 这两种抽象数据结构底层实现可以是链表、树、哈希表。但根据是否保留数据的顺序性，我们可以将它们分类如下：
 *      1. 有序集合、有序映射：底层是平衡树（在 java 中对应的是 TreeSet, TreeMap）
 *      2. 无序集合、无序映射：底层是哈希表（在 java 中对应的是 HashSet, HashMap）
 *
 * - 实现中的一个 bug：
 *   HashTable<K, V> 中的 K 不要求是 Comparable 的，但 TreeMap<K, V> 中的 K 却要求，因此产生了冲突，这是个 bug。
 * */

public class HashTable<K, V> {  // K 不再需要实现 Comparable，因为比较的方式变成了比哈希值，因此只要实现 hashCode 方法即可，而 hashCode 方法是定义在 Object 上的，所有变量都有。
    private static final int upperTol = 10;  // 数组中每个位置平均最多存储10个元素，>= 10 的话就要扩容
    private static final int lowerTol = 2;   // <= 2 的话就要缩容
    private final int[] capacity = { 53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157, 98317,
            196613, 393241, 786433, 1572869, 3145739, 6291469, 12582917, 25165843, 50331653, 100663319,
            201326611, 402653189, 805306457, 1610612741};
    private int capacityIndex = 0;   // 数组的初始容量索引

    private TreeMap<K, V>[] hashTable;  // 这里实现的哈希表实际上是一个 TreeMap 数组（使用了基于 TreeMap 的 separate chaining 解决哈希冲突）
    private int M;     // 开辟的数组大小（对哈希表的性能有直接影响）
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

    // 哈希函数：将给定的 key 转化成数组索引（即哈希值）
    // 1. 调用 key 上的 hashCode 方法，将可能为任意类型的 key 转化为整型
    // 2. 将得到的整型通过位与操作消除符号（相当于取绝对值），再模上 M，最终得到哈希值
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
