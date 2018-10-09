package Queue;

import Array.Array;

/*
* Complexity Analysis
* ArrayQueue<E>
*     - void enqueue(E e);  O(n) 均摊
*     - E dequeue();        O(n)
*     - E getFront();       O(1)
*     - int getSize();      O(1)
*     - boolean isEmpty();  O(1)
* */

public class ArrayQueue<E> implements Queue<E> {
    private Array<E> array;

    public ArrayQueue(int capacity) { array = new Array<E>(capacity); }

    public ArrayQueue() { array = new Array<E>(); }

    public void enqueue(E e) { array.addLast(e); }

    public E dequeue() { return array.removeFirst(); }

    public E getFront() { return array.getFirst(); }

    public int getSize() { return array.getSize(); }

    public boolean isEmpty() { return array.isEmpty(); }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d, ", getSize()));
        s.append("front -> [");
        for (int i = 0; i < getSize(); i++) {
            s.append(array.get(i));
            if (i != getSize() - 1)
                s.append(", ");
        }
        s.append("] <- tail\n");
        return s.toString();
    }
}
