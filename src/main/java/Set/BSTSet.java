package Set;

import BST.BST;

/*
* 基于 BST 的 Set（因为我们实现的 BST 就是不允许存在重复元素的，因此用来做 Set 的底层数据结构非常理想）
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
