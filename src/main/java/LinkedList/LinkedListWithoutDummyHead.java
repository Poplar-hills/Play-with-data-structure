package LinkedList;

/*
 * Linked List
 *
 * - 背景：之前实现的动态 Array 是通过 resize 机制实现其动态性，但底层还是基于静态数组。而链表是一种真正的动态数据结构，
 *   同时也是最简单的动态数据结构。更复杂的动态数据结构还有 BST、Trie、AVLTree 等等。
 *
 * - 基本形态：1 -> 2 -> 3 -> null
 *   - 其中每一个 node 包含两部分内容：{ e: value, next: node }
 *   - 链表的最后一个元素一定是 null
 *
 * - 2个天然特性：
 *   1. 动态性（不需要想静态数组那样一次开辟一片空间，而是每次添加/删除元素都是在扩容/缩容）
 *   2. 递归性（几乎所有操作都可通过递归实现）
 *
 * - 优势：
 *   1. 动态性（可根任意扩容缩容；lower memory overhead：无内存空间浪费、扩容缩容时无需重新重新分配内存）
 *   2. 插入、删除元素效率极高（都是 O(1)，不会像数组一样，向头部或中间插入或删除元素时需要遍历移动后面的所有元素）
 *   3. 实现简单（相对于复杂数据结构来说）
 * - 劣势：
 *   1. 丧失了数组的随机访问能力（无法通过 index 访问元素）
 *   2. 遍历效率较低（因为链表的节点在内存中不是连续存储的，如果需要频繁遍历链表，可能是选错了数据结构）
 *
 * - 应用场景：
 *   - 实现其他数据结构：
 *     1. FIFO queue - 因为要一端入队一端出队，若基于数组实现则需额外维护 head, tail 两个指针。实际上 stack，queue
 *        都可以用链表实现，只是 FIFO queue 用链表比用数组实现优势更明显。
 *     2. Graph - 邻接表（adjacent list）可以用链表数组表示（其实用链表数组、二维数组、链表字典、数组字典都可以，没有太大区别）。
 *     3. Sparse matrices - 同上
 *   - 生活场景：所有有"前进"、"后退"的功能：如浏览器、音乐播放器、图片浏览器等（但若有排序需求就不能用链表实现了）。
 * */

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

    // 一般不会在链表任意位置添加节点（如果需要这种操作，就不应该选择这种数据结构），但面试题中可能会出现
    public void addAtIndex(E e, int index) {  // 把中间的过程可视化出来会更清楚
        if (index < 0 || index > size)
            throw new IllegalArgumentException("addAtIndex failed. Reuqires index < 0 || index > size");

        if (index == 0) {  // LinkedList 头结点没有前一个结点，所以特殊处理
            addFirst(e);
        } else {
            Node prev = head;  // 链表的添加、删除都需要用的前一个节点
            for (int i = 0; i != index - 1; i++)
                prev = prev.next;  // 顺藤摸瓜找到待添加位置的前一个位置上的 node
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
