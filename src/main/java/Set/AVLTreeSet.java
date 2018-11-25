package Set;

import AVLTree.AVLTree;

/*
* - AVLTreeSet 的底层使用 AVLTree 实现。
* - AVLTreeSet 也可以使用 AVLTreeMap 实现（Map 只关注 key 而忽略 value 即是 Set）。
* */

public class AVLTreeSet<E extends Comparable<E>> implements Set<E> {
    private AVLTree<E> avlTree;  // 如果使用 AVLTreeMap 实现的话则写作：AVLTreeMap<E, Object> 即可（不需要关注 value）

    public AVLTreeSet() { avlTree = new AVLTree<E>(); }

    @Override
    public void add(E e) { avlTree.add(e); }  // 如果使用 AVLTreeMap 实现的话则写作：avlTreeMap.add(e, null) 即可

    @Override
    public void remove(E e) { avlTree.remove(e); }

    @Override
    public boolean contains(E e) { return avlTree.contains(e); }

    @Override
    public int getSize() { return avlTree.getSize(); }

    @Override
    public boolean isEmpty() { return avlTree.isEmpty(); }
}
