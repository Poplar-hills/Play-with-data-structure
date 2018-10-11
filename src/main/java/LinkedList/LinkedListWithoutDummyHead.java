package LinkedList;

/*
* - Array, Stack, Queue, LinkedList 都是线性数据结构
* - Java 的 Array 本身是静态数据结构，而之前实现的带有 resize() 是对 Array 的扩展，使其带有一定动态性，但仍然存在一定空间浪费的情况。
*   而 LinkedList 是真正的动态数据结构（也是虽简单的一种），它的大小完全是根据需求而定的，因此需要动态、线性数据结构的情况就应该选它。
* - LinkedList 的优点是其动态性，不需要手动处理固定容量的问题；而缺点是丧失了 Array 的随机访问（通过 index 访问）的能力，这是因为它
*   在内存中的存储方式不再是连续的，因此不能像 Array 那样通过 index 访问。
* - LinkedList 中的每一个 node 包含两部分内容：1. data 2. next。注意 LinkedList 的最后一个 node 的 next 为 null。
 * */

import Array.Array;

public class LinkedListWithoutDummyHead<E> {

    private Node head;  // Array、ArrayStack、ArrayQueue（通过 size）只追踪尾；LinkedList 只追踪头；LoopQueue 头尾都追踪
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

    public LinkedListWithoutDummyHead() {  // LinkedList 中一个节点都没有时的构造函数
        head = null;
        size = 0;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void addAtIndex(E e, int index) {  // 把中间的过程可视化出来会更清楚
        if (index < 0 || index > size)
            throw new IllegalArgumentException("addAtIndex failed. Reuqires index < 0 || index > size");

        if (index == 0) {  // LinkedList 头结点没有前一个结点，所以特殊处理
            addFirst(e);
        } else {
            Node prev = head;
            for (int i = 0; i != index - 1; i++)
                prev = prev.next;  // 水藤摸瓜找到待添加位置的前一个位置上的 node
            prev.next = new Node(e, prev.next);
            size++;
        }
    }

    public void addFirst(E e) {
        head = new Node(e, head);  // 用旧的 head node 作为新的 head node 的 next
        size++;
    }

    public void addLast(E e) { addAtIndex(e, size); }
}
