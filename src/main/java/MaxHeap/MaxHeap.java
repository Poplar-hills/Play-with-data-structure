package MaxHeap;

/*
* - 树型结构有很多变体：BST、AVL tree、Red-black tree、Heap、Trie、Segment tree 等，堆（heap）是其中一种。
* - 在了解堆之前需要先了解"优先队列"（Priority Queue）：一种出队顺序和入队顺序无关，仅和优先级相关的结构。
*   SEE: Queue - PriorityQueue
* - 堆有很多种，这里我们实现最主流的一种堆：使用二叉树表示的堆，即二叉堆（Binary Heap）。
* - 二叉堆是一种满足一些特殊性质的二叉树，其特殊性质为：
*   1. 是一棵完全二叉树（complete binary tree）
*   2. 堆中任意一个节点的值都 >= 它任意孩子节点的值
*                  62
*                /    \
*              41      30
*            /   \    /  \
*          28    16  22  13
*        /  \   /
*      19   17 15
*   这样的堆叫"最大堆"（max heap）；相反，若堆中任意一个节点的值都 <= 它的子节点的值，则叫"最小堆"（min heap）。
*
* - 实现：因为二叉堆是一个完全二叉树，而表达完全二叉树有一个经典的实现方式：因为树中的元素是从上到下、从大到小一层
*   一层排列的，因此可以用数组的形式来表示这棵树：[62, 41, 30, 28, 16, 22, 13, 19, 17, 15]。
* - 用数组表示二叉堆时，唯一需要解决的问题就是如何找到某一个节点的左右孩子。而用数组表示后，我们可以找到规律：
*   设某一个节点在数组中的索引是 i，则有：
*     - 其左孩子的索引: 2 * i + 1
*     - 其右孩子的索引: 2 * i + 2 (右孩子总是在左孩子右边)
*     - 其父节点的索引: (i - 1) / 2 (整型除法会抹去小数)（能找到父节点索引，这是用数组表示的额外好处）
*
* - 对于堆来说，唯一的取值操作就是取出堆顶元素（对于最大堆来说就是取出最大值，最小堆就是取出最小值）。
*
* - 二叉堆的时间复杂度：因为二叉堆是一棵完全二叉树，因此永远不会退化成链表。因此其节点个数和高度之间的关系永远都是
*   logn 级别的关系。因此其 add 和 extractMax 操作的时间复杂度永远都是 O(logn)。
*
* - 用任意数组生成最大堆（heapify）：
*   - 因为最大堆可以用数组来表示，因此给定任意数组，只要合理地交换数组中的元素就能将其整理成最大堆的形态，有2种方法：
*     1. 将数组的元素逐一添加到一个空堆中
*     2. heapify：从后往前逐一将数组中的非叶子节点进行 sift down 操作，SEE: https://coding.imooc.com/lesson/207.html#mid=13742（3'22''）
*   - 这两种方式生成的结果不一定一样，但都是最大堆。
*   - 复杂度上来看，heapify 要比方法1中的方法快，因为：
*     - 方法1中，将 n 个元素逐一插入空堆是 O(n * logn) 即 O(nlogn) 的复杂度；
*     - 而 heapify 的复杂度是 O(n)，其计算过程较复杂，只要知道 heapify 是由从后往前第一个非叶子节点开始逐一进行
*       siftDown 操作，因此相当于一上来就把 n/2 个元素刨除掉了，因此相对更快。
*
* - 注：heapify 方法中，data = new Array<>(); 这句的复杂度可以近似为 O(1)：
*   - 我们可以近似的认为，一次开辟空间操作（一次new操作）是一个 O(1) 级别的操作。具体操作系统是如何找到这片空间的、
*     找不到可用的空间如何处理……这些问题都是操作系统层面的问题，由操作系统内部机制进行处理，我们只需知道，每次 new
*     都看作是操作系统提供的一个常数级操作即可。
*   - 从另一个角度来看，算法复杂度分析考虑的是排除了外在条件影响后的算法本身的效率，内容如何分配、内存是否不足……这
*     些都属于外在条件影响，在分析算法复杂度时可以忽略，但在分析和优化算法实际运行效率时需要考虑。
* */

import Array.Array;

public class MaxHeap<E extends Comparable<E>> {
    private Array<E> data;

    public MaxHeap(int capacity) { data = new Array<E>(capacity); }

    public MaxHeap() { data = new Array<E>(); }

    public MaxHeap(E[] arr) {  // 通过任意数组生成一个最大堆的构造函数（即 heapify 过程）
        data = new Array<E>(arr);  // 先将传入数组转换成我们动态数组（需要给 Array 添加一个新的构造器）
        int lastNonLeafNodeIndex = getParentIndex(getSize() - 1);  // 找到最后一个非叶子节点的索引（即最后一个节点的父节点的索引）
        for (int i = lastNonLeafNodeIndex; i >= 0; i--)  // 从最后一个非叶子节点向前遍历，对每个非叶子节点进行 siftDown
            siftDown(i);
    }

    /*
     * 辅助方法
     **/
    private int getParentIndex(int index) {  // 返回用数组实现的完全二叉树中，一个索引处的元素在树中的父节点的索引
        if (index == 0)
            new IllegalArgumentException("parent failed. The element at index 0 doesn't have parent.");
        return (index - 1) / 2;  // 整型除法，小数会被抹去
    }

    private int getLeftChildIndex(int index) {  // 返回用数组实现的完全二叉树中，一个索引处的元素在树中的左孩子的索引
        return index * 2 + 1;
    }

    private int getRightChildIndex(int index) {  // 返回用数组实现的完全二叉树中，一个索引处的元素在树中的右孩子的索引
        return getLeftChildIndex(index) + 1;
    }

    private void siftUp(int k) {
        while (k > 0 && data.get(getParentIndex(k)).compareTo(data.get(k)) < 0) {
            data.swap(k, getParentIndex(k));
            k = getParentIndex(k);
        }
    }

    private void siftDown(int k) {
        while (getLeftChildIndex(k) < getSize()) {  // 只要左孩子的索引 < 元素个数就说明还没到达叶子节点，可以继续循环
            // 找到位于 k 的节点的左右孩子中较大的那个的索引
            int i = getLeftChildIndex(k);
            if (i + 1 < getSize() && data.get(i + 1).compareTo(data.get(i)) > 0)  // i+1 是右孩子的索引，i+1 < getSize() 是要保证有右孩子
                i += 1;  // i 保存了左右孩子中值较大的那个的索引

            // 用父节点与较大的那个比，如果父节点大则 break loop，否则 swap（只有用较大的子节点跟父节点比才能保证 swap 之后换上来的新父节点比两个子节点都大，保证最大堆性质不被破坏）
            if (data.get(k).compareTo(data.get(i)) >= 0)
                break;

            data.swap(k, i);
            k = i;  // 记得最后要让 while 循环进入下一轮
        }
    }

    /*
     * 添操作
     * */
    public void add(E e) {  // 向堆中添加元素
        data.addLast(e);  // 先添加到数组末尾，即完全二叉树的最后
        siftUp(getSize() - 1);  // 上浮，添加节点不能破坏前面说的二叉堆的第二个性质，因此要与该节点路径上的祖先节点一一比较，如果大于祖先节点则交换
    }

    /*
     * 取操作
     * - 最大值永远都在堆顶上，因此取出最大值后需要填补树顶上的节点。这里的策略是取数组的最后一个元素填补上，然后再对其进行下沉操作，
     *   来保证不破坏二叉堆的第二个性质。
     * */
    public E extractMax() {
        E ret = findMax();
        data.set(0, data.getLast());
        data.removeLast();
        siftDown(0);
        return ret;
    }

    /*
    * 改操作
    * - 该操作相当于 extractMax + add(e)。但这两个方法组合的话就是2次 O(logn) 的复杂度，稍微低效了点。更高效的做法是，直接用 e 替换
    *   堆顶元素，然后再 siftDown，这样的复杂度是1次 O(logn)。
    * */
    public E replace(E e) {
        E ret = findMax();
        data.set(0, e);
        siftDown(0);
        return ret;
    }

    /*
     * 查操作
     * */
    public E findMax() { return data.getFirst(); }

    public int getSize() { return data.getSize(); }

    public boolean isEmpty() { return data.isEmpty(); }

    /*
    * Misc
    * */
    @Override
    public String toString() { return data.toString(); }  // 打印用数组表示的最大堆
}
