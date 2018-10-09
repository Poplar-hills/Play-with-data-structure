package Queue;

/*
* - 在 LinkedList.java 中实现的链表，对链表头的操作都是 O(1)，而对链表尾的操作都是 O(n)，因此如果基于它来实现队列则 enqueue 或
*   dequeue 中一定会有一个是 O(n)，因此需要对链表进行改造。改造方法就是：增加一个 tail 指针，指向链表最后一个元素，这样对链表尾的
*   增操作就可以是 O(1) 了，而删操作仍然是 O(n)，因此链表尾可以作为队列的尾（只进不出）；而链表的头可以作为队列的头（只出不进）。
*   这样实现出来的队列的 enqueue 和 dequeue 就都是 O(1) 复杂度了。
* */

public class LinkedListQueue<E> implements Queue<E> {
    private Node head, tail;
    private int size;

    private class Node {
        public E e;
        public Node next;

        public Node(E e, Node next) {
            this.e = e;
            this.next = next;
        }

        public Node(E e) { this(e, null); }

        public Node() { this(null, null); }

        @Override
        public String toString() { return e.toString(); }
    }

    public LinkedListQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(E e) {
        if (getSize() == 0) {
            head = tail = new Node(e);
        } else {
            tail.next = new Node(e);  // 从链表尾一端添加元素
            tail = tail.next;
        }
        size++;
    }

    public E dequeue() {
        if (isEmpty())
            throw new IllegalArgumentException("dequeue failed. Cannot dequeue from an empty queue.");

        Node retNode = head;
        head = head.next;
        retNode.next = null;
        size--;
        if (size == 0) tail = null;  // size 为0说明之前链表中只有一个元素，head 和 tail 都指向它，当它被 dequeue 之后，需要维护一下 tail
        return retNode.e;
    }

    public E getFront() {
        if (isEmpty())
            throw new IllegalArgumentException("getHead failed. Queue is empty");
        return head.e;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("Queue: front [");
        for (Node curr = head; curr != null; curr = curr.next)
            s.append(curr + " -> ");
        s.append("null] tail");

        return s.toString();
    }
}
