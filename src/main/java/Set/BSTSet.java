package Set;

import BST.BST;

/*
* - 集合是一种抽象数据结构，和底层实现无关，因此这里将它实现成为接口，而底层实现是基于 BST 的（因为我们实现的 BST 就是不
*   允许存在重复元素的，因此用来做 Set 的底层数据结构非常理想）。
* - 基于 BST 的 Set 是一种"有序集合"，即集合中的元素具有顺序性，可以方便地从小到大遍历。这种数据结构通常提供多种跟顺序相
*   关的方法，如找出所存储元素的最大值(maximum)，最小值(minimum)，某个元素的下一个元素(succesor)，上一个元素(predecessor)，
*   第k大元素(select)和某个元素是第几大元素(rank)。
* - 通常使用 BST，有序数组或者有序链表，都可以作为“有序集合”的底层实现。只不过这里我们实现的 BSTSet 没有为我们的二分搜索
*   树实现上述接口。
* - 注："有序性"的解释 SEE: https://coding.imooc.com/learn/questiondetail/81002.html
* - 基于无序链表、无序数组、哈希表实现的 Set 则是"无序集合"，即集合中的元素没有顺序性。
* */

public class BSTSet<E extends Comparable<E>> implements Set<E> {
    private BST<E> bst;

    public BSTSet() {
        bst = new BST<E>();
    }

    public void add(E e) { bst.add(e); }

    public void remove(E e) { bst.remove(e); }

    public boolean contains(E e) { return bst.contains(e); }

    public int getSize() { return bst.getSize(); }

    public boolean isEmpty() { return bst.isEmpty(); }
}
