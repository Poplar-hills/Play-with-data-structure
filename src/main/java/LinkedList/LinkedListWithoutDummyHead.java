package LinkedList;

/*
 * - 我们之前实现的动态 Array 是通过 resize 机制实现其动态性，但底层还是基于静态数组。而链表是一种真正的动态数据结构，
 *   同时也是最简单的动态数据结构。更复杂的动态数据结构还有 BST、Trie、AVLTree 等等。
 * - 链表的2个天生特性：
 *     1.动态性（不需要想静态数组那样一次开辟一片空间，可根据需要随时扩容缩容）
 *     2.递归性（几乎所有操作都可通过递归实现）
 * - 链表的优劣势：
 *     - 优势：动态性（可根据需要随时扩容缩容，无空间浪费）
 *     - 劣势：丧失了数组的随机访问能力（无法通过 index 访问元素，链表的节点在内存中不是连续存储的）
 * - 链表的基本形态：1 -> 2 -> 3 -> null
 *   - 其中中的每一个 node 包含两部分内容：1.data  2.next
 *   - 最后一个元素一定是 null
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
