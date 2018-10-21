package Set;

import LinkedList.LinkedList;

public class LinkedListSet<E> implements Set<E> {
    private LinkedList list;

    public LinkedListSet() { list = new LinkedList(); }

    public void add(E e) {
        if (!list.contains(e))  // 不能添加重复元素
            list.addFirst(e);
    }

    public void remove(E e) { list.removeElement(e); }

    public boolean contains(E e) { return list.contains(e); }

    public int getSize() { return list.getSize(); }

    public boolean isEmpty() { return list.isEmpty(); }
}
