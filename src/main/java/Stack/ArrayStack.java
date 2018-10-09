package Stack;

import Array.Array;

/*
 * Complexity Analysis
 * ArrayStack<E>
 *     - void push(E e);     O(n) 均摊
 *     - E pop();            O(1)
 *     - E peek();           O(1)
 *     - int getSize();      O(1)
 *     - boolean isEmpty();  O(1)
 * */

public class ArrayStack<E> implements Stack<E> {
    private Array<E> array;

    public ArrayStack(int capacity) { array = new Array<E>(capacity); }

    public ArrayStack() { this(10); }

    public int getSize() {
        return array.getSize();
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public void push(E e) { array.addLast(e); }

    public E pop() { return array.removeLast(); }

    public E peek() { return array.getLast(); }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d ", getSize()));
        s.append("[");
        for (int i = 0; i < getSize(); i++) {
            s.append(array.get(i));
            if (i != getSize() - 1)
                s.append(", ");
        }
        s.append("] <- top \n");
        return s.toString();
    }
}
