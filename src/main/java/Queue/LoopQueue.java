package Queue;

/*
* LoopQueue<E>
*     - 循环队列的意义是为了解决普通队列在 dequeue 的时候效率低（复杂度是 O(n)）的问题
*     - LoopQueue 特点是在 dequeue 操作之后不前移所有元素，而是引入 front、tail 两个指针，充分利用 dequeue 之后空出来的位置
*     - 队列为空时：front == tail
*     - 队列已满时：
*       - 如果队列中没有循环：|0|1|2|3|4| |，此时 front 指0，tail 指最后的空元素；
*       - 如果队列中有循环（有过 dequeue 操作）：|0|1|2|3|4| | -> |n|1|2|3|4|5| -> |6|n|2|3|4|5|，此时 front 指2，tail 指6后面的 null
*       - 统一这2种情况：当 front == (tail + 1) % data.length 时，队列已满
*     - LoopQueue 会有意将一个位置空出来，不保存新元素，以保证队列为空和队列已满时的条件不都是 front == tail。
* */

public class LoopQueue<E> implements Queue<E> {
    private E[] data;
    private int front, tail;
    private int size;  // 可以不要（作业题）

    public LoopQueue(int capacity) {
        data = (E[]) new Object[capacity + 1];  // 不再使用之前创建的 Array 类。另外 capacity + 1 是因为循环队列中会有一个空间被浪费掉
        front = 0;
        tail = 0;
        size = 0;
    }

    public LoopQueue() { this(10); }

    public int getSize() {
        return size;
    }

    public int getCapacity() { return data.length - 1; }  // 此处 - 1 也是因为会浪费掉一个空间，真正可利用的空间是 data.length - 1

    public void enqueue(E e) {
        if (isFull())
            resize(getCapacity() * 2);

        data[tail] = e;  // 新元素放在 tail 位置
        tail = (tail + 1) % data.length;  // 入栈的时候 front 不变，tail 变
        size++;
    }

    public E dequeue() {
        if (isEmpty())
            throw new IllegalArgumentException("dequeue failed. Empty queue");

        E dequeued = data[front];
        data[front] = null;  // 删除的是 front 位置上的元素
        front = (front + 1) % data.length;  // 出栈的时候 tail 不变，front 变
        size--;

        if (size == getCapacity() / 4 && getCapacity() / 2 != 0)
            resize(getCapacity() / 2);

        return dequeued;
    }

    public E getFront() {
        if (isEmpty())
            throw new IllegalArgumentException("getFront failed. Empty queue");

        return data[front];
    }

    private boolean isFull() {
        return (tail + 1) % data.length == front;
    }

    public boolean isEmpty() {
        return front == tail;
    }

    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity + 1];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(i + front) % data.length];  // i + front 可能会越界（超过 data.length），所以要求余
        }
        data = newData;
        front = 0;
        tail = size;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d, Capacity = %d\n", size, getCapacity()));
        s.append("[");
        for (int i = front; i != tail; i = (i + 1) % data.length) {
            s.append(data[i]);
            if ((i + 1) % data.length != tail)
                s.append(", ");
        }
        s.append("], ");
        s.append(String.format("front: %d, tail: %d\n", front, tail));
        return s.toString();
    }
}
