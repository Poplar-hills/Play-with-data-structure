package Queue;

import Array.Array;

/*
* - 相对于 Stack 的先入后出，Queue 是先入先出。
* - 最基本队列是基于数组的，只需要做一些限制：只能从数组尾部入队，而从头部出队
* - Complexity Analysis
*   - void enqueue(E e);  O(1) 均摊
*   - E dequeue();        O(n) 因为要从数组头部移除元素
*   - E getFront();       O(1)
*   - int getSize();      O(1)
*   - boolean isEmpty();  O(1)
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
