package Queue;

import Array.Array;

/*
* - 相对于 Stack 的先入后出，Queue 是先入先出（FIFO）。
* - 这里实现的队列是在数组之上加一些限制：只能从数组尾部入队，只能数组从头部出队（尾部 enqueue，头部 dequeue）
* - 复杂度分析：
*   - enqueue   O(1)  均摊（均摊了扩容的复杂度）
*   - dequeue   O(n)  因为要从数组头部移除元素，也是均摊的（均摊了缩容的复杂度）
*   - getFront  O(1)
*   - getSize   O(1)
*   - isEmpty   O(1)
* - 可见 ArrayQueue 的性能问题在于出队操作是 O(n) 复杂度，我们可以使用循环队列解决这一问题，实现 SEE: LoopQueue
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
