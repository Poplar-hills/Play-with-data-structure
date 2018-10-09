package Stack;

import LinkedList.LinkedList;

public class LinkedListStack<E> implements Stack<E> {
    private LinkedList<E> list;  // 封装 linkedList

    public LinkedListStack() { list = new LinkedList<E>(); }

    public int getSize() { return list.getSize(); }

    public boolean isEmpty() { return list.isEmpty(); }

    public void push(E e) { list.addFirst(e); }

    public E pop() { return list.removeFirst(); }

    public E peek() { return list.getFirst(); }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("top -> [");
        s.append(list);
        s.append("]");
        return s.toString();
    }
}
