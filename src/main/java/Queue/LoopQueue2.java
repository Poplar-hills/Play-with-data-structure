package Queue;

/*
* 这个版本的 LoopQueue 改进了判断已满和为空的方式，不需要再浪费一个空间了。
* */

public class LoopQueue2<E> implements Queue<E> {
    private E[] data;
    private int front, tail;
    private int size;

    public LoopQueue2(int capacity) {
        data = (E[]) new Object[capacity];  // 这一版没有浪费，所以不需要像之前那样开辟 capacity + 1 的空间
        front = tail = size = 0;
    }

    public LoopQueue2() { this(10); }

    public int getSize() { return size; }

    public int getCapacity() { return data.length; }  // 没有浪费，所以不需要像之前那样 data.length + 1

    public void enqueue(E e) {
        if (isFull())
            resize(getCapacity() * 2);

        data[tail] = e;
        tail = (tail + 1) % data.length;
        size++;
    }

    public E dequeue() {
        if (isEmpty())
            throw new IllegalArgumentException("dequeue failed. Empty queue");

        E dequeued = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
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

    private boolean isFull() {  // 不再使用 front 和 tail 之间的关系来判断队列是否为满，而直接使用 size
        return size == getCapacity();
    }

    public boolean isEmpty() {  // 直接使用 size
        return size == 0;
    }

    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity];  // 不再需要 newCapacity + 1
        for (int i = 0; i < size; i++)
            newData[i] = data[(i + front) % data.length];
        data = newData;
        front = 0;
        tail = size;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d, Capacity = %d\n", size, getCapacity()));
        s.append("[");
        for (int i = 0; i < size; i++) {  // 不能再是写成 for (int i = front; i != tail; ...) 了。因为 tail 可能等于 front（此时队列可能为空也可能已满）。
            s.append(data[(front + i) % data.length]);
            if ((front + i + 1) % data.length != tail)  // 如果不是最后一个节点
                s.append(", ");
        }
        s.append("], ");
        s.append(String.format("front: %d, tail: %d\n", front, tail));
        return s.toString();
    }

}
