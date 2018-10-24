package MaxHeap;

/*
* - 堆本身也是树形结构。
* - 堆有很多种，这里我们实现最主流的一种堆：使用二叉树表示的堆，即二叉堆（Binary Heap）。
* - 二叉堆是一种满足一些特殊性质的二叉树，其特殊性质为：
*   1. 是一棵完全二叉树（complete binary tree）
*   2. 堆中任意一个节点的值都 >= 它的孩子节点的值
*              62
*            /    \
*          41      30
*        /   \    /  \
*      28    16  22  13
*    /  \   /
*  19   17 15
*   这样得到的堆也叫"最大堆"（max heap）；相反，若堆中任意一个节点的值都 >= 它的孩子节点的值，则叫"最小堆"（min heap）。
*
* - 在定义清楚二叉堆之后，来看如何实现。因为二叉堆是一个完全二叉树，而表达完全二叉树有一个巧妙的手段：因为树中的元素是从上到下、
*   从大到小一层一层排列的，因此可以用数组的形式来表示这棵树：[62, 41, 30, 28, 16, 22, 13, 19, 17, 15]。
* - 用数组表示二叉堆时，唯一需要解决的问题就是如何找到某一个节点的左右孩子。而用数组表示后，我们可以找到规律：
*   某一个节点在数组中的 index 是 i，则有：
*     - 其左孩子的 index: 2 * i + 1
*     - 其右孩子的 index: 2 * i + 2 (右孩子总是在左孩子右边)
*     - 其父节点的 index: (i - 1) / 2 (整型除法，抹去小数)（还能找到父节点，这是用数组表示的额外好处）
*
* - 对于堆这种数据结构来说，唯一的取值操作就是取出堆顶元素。
*
* - 二叉堆的时间复杂度：因为二叉堆是一棵完全二叉树，因此永远不会退化成链表。因此其节点个数和高度之间的关系永远都是 logn 级别的
*   关系。因此其 add 和 extractMax 操作的时间复杂度永远都是 O(logn)。
* */

import Array.Array;

public class MaxHeap<E extends Comparable<E>> {
    private Array<E> data;

    public MaxHeap(int capacity) { data = new Array<E>(capacity); }

    public MaxHeap() { data = new Array<E>(); }

    public MaxHeap(E[] arr) {  // 通过任意数组生成一个最大堆的构造函数
        // 先将传入数组转换成我们自己封装的动态数组
        data = new Array<E>(arr);

        // heapify（将任意数组，如 [15, 17, 19, 15, 22, 16, 28, 30, 41, 62]，整理成最大堆的形状）
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
        while (getLeftChildIndex(k) < data.getSize()) {  // 只要左孩子的索引 > 元素个数，则说明还没到达叶子节点，可以继续循环
            // 找到位于 k 的节点的左右孩子中较大的那个的索引
            int i = getLeftChildIndex(k);
            if (i + 1 < data.getSize() && data.get(i).compareTo(data.get(i + 1)) < 0)  // i 是左孩子的索引，i + 1 即为右孩子的索引
                i = i + 1;  // i 保存了左右孩子中值较大的那个的索引

            // 用较大的那个与父节点比较，如果父节点大，则 break loop，否则 swap
            if (data.get(k).compareTo(data.get(i)) >= 0)
                break;
            data.swap(k, i);
            k = i;
        }
    }

    /*
     * 添操作
     * */
    public void add(E e) {  // 向堆中添加元素
        data.addLast(e);  // 先添加到数组末尾，即完全二叉树的最后
        siftUp(data.getSize() - 1);  // 上浮，添加节点不能破坏前面说的二叉堆的第二个性质，因此要与该节点路径上的祖先节点一一比较，如果大于祖先节点则交换
    }

    /*
     * 取操作
     * */
    public E extractMax() {  // 最大堆的最大值都在树顶上，因此 extract 最大值后需要填补树顶上的节点，这里的策略是取最后一个节点进行填补，然后在 siftDown，从而不破坏二叉堆的第二个性质
        E ret = findMax();
        data.set(0, data.getLast());
        data.removeLast();
        siftDown(0);
        return ret;
    }

    /*
    * 改操作
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
