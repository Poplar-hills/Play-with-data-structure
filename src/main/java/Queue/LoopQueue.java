package Queue;

/*
* - 循环队列的意义是为了解决普通队列在 dequeue 的时候效率低（复杂度是 O(n)）的问题。
* - LoopQueue 特点是在 dequeue 操作之后不前移所有元素，而是引入 front、tail 两个指针，充分利用 dequeue 之后空出来的位置。
* - 循环队列 enqueue、dequeue 的过程：
*                  | | | | | |   front=0, tail=0
*     enqueue(a):  |a| | | | |   front=0, tail=1
*     enqueue(b):  |a|b| | | |   front=0, tail=2
*     enqueue(c):  |a|b|c| | |   front=0, tail=3
*     enqueue(d):  |a|b|c|d| |   front=0, tail=4（此时队列已满）
*     dequeue() :  | |b|c|d| |   front=1, tail=4
*     enqueue(e):  | |b|c|d|e|   front=1, tail=0（此时队列已满）
*     dequeue() :  | | |c|d|e|   front=2, tail=0
*     enqueue(f):  |f| |c|d|e|   front=2, tail=1（此时队列已满）
*
*   - 队列为空时：front == tail
*   - 队列已满时：front == (tail + 1) % arr.length
*     注意：队列已满时会有一个位置空为空，不存储元素，以保证队列为空和队列已满时的条件（front == tail）不相同。
* 
* - 实现：因为循环队列的机制已经不同于 Array 了，因此不能像 ArrayStack 或 ArrayQueue 一样基于 Array 实现。
*
* - 复杂度分析：
*   - enqueue  O(1)  均摊（均摊了扩容的复杂度）
*   - dequeue  O(1)  均摊（均摊了缩容的复杂度）
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

    public int getSize() { return size; }

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
