package LinkedList;

/*
 * - 注：先看 LinkedListWithoutDummyHead.java，再看这里的实现。
 * - LinkedListWithoutDummyHead 的实现没有问题，只是不够优雅，因为在对任意位置添加结点的时候需要区别 index 是否是 0。
 * - 解决办法是通过在 LinkedList 最前面添加一个虚拟结点（dummyHead）统一两种情况，这是一种常见技巧。
 * - 时间复杂度分析：对于数组来说 addLast 是 O(1)，而对于链表来说：
 *   - addFirst 和 removeFirst 是 O(1)
 *   - addLast 和 removeLast 是 O(n)
 *   - addAtIndex 和 removeAtIndex 平均是 O(n/2)，所以也是 O(n)
 *   - 所以综合来看，链表的增和删操作的复杂度都是 O(n)
 *   - 对于链表的改操作（set），因为链表不支持随机访问，因此是其复杂度是 O(n)
 *   - 对于链表的查操作（contains, get），都需要遍历链表，因此复杂度都是 O(n)
 *   - 因此综合来看，链表的增、删、改、查都是 O(n) 复杂度。
 *   - 可见链表的优势不在于其时间效率，而在于其空间效率（其动态性使得没有空间浪费）。
 *   - 另外，对于链表头的增、删、改、查操作都是 O(1)，因此非常适合作为栈的底层实现；链表也可以作为 Queue 的底层实现，但需要
 *     进行改造（增加一个 tail 指针，使得 dequeue 操作的复杂度也是 O(1)），具体实现 SEE: LinkedListQueue.java
 * */

import Array.Array;
import javafx.util.Pair;

public class LinkedList<E> {

    private Node dummyHead;
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

    public LinkedList() {
        dummyHead = new Node(null, null);  // 初始化的时候生成 dummy head node
        size = 0;
    }

    public LinkedList(E[] arr) {  // 通过数组生成链表的构造函数
        if (arr == null || arr.length == 0)
            throw new IllegalArgumentException("Invalid array");

        dummyHead = new Node(null, null);
        Node curr = dummyHead;
        for (int i = 0; i < arr.length; i++) {
            curr.next = new Node(arr[i], null);
            curr = curr.next;
            size++;
        }
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    /*
    * 增操作
    * */
    // 一般不会在链表任意位置添加节点（如需这种操作，很有可能不该选这种数据结构），但面试题中可能会出现
    public void addAtIndex(E e, int index) {  // 增操作的递归实现
        if (index < 0 || index > size)
            throw new IllegalArgumentException("addAtIndex failed. Reuqires index < 0 || index > size");
        dummyHead = add(dummyHead, e, index, 0);
    }

    private Node add(Node prev, E e, int index, int depth) {
        if (depth == index) {
            prev.next = new Node(e, prev.next);
            size++;
        } else
            prev.next = add(prev.next, e, index, depth + 1);
        return prev;
    }

    public void addAtIndexNR(E e, int index) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("addAtIndexNR failed. Reuqires index < 0 || index > size");

        Node prev = dummyHead;
        for (int i = 0; i != index; i++)
            prev = prev.next;  // 不再是 index - 1，因为多了一个 dummy head node
        prev.next = new Node(e, prev.next);
        size++;
    }

    public void addFirst(E e) { addAtIndex(e, 0); }

    public void addLast(E e) { addAtIndex(e, size); }

    /*
     * 删操作
     * */
    // 一般不会在链表任意位置删除节点（如需这种操作，很有可能不该选这种数据结构），用的更多的是从链表中删除某一元素，见 removeElement。
    public E removeAtIndex(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("removeAtIndex failed. Reuqires index < 0 || index > size");
        Node retNode = remove(dummyHead, index, 0);
        return (E) retNode.e;
    }

    private Node remove(Node prev, int index, int depth) {
        if (index == depth) {
            Node retNode = prev.next;  // 待删除节点
            prev.next = retNode.next;
            retNode.next = null;
            size--;
            return retNode;
        }
        return remove(prev.next, index, depth + 1);
    }

    public E removeAtIndexNR(int index) {  // 非递归实现
        if (index < 0 || index > size)
            throw new IllegalArgumentException("removeAtIndex failed. Reuqires index < 0 || index > size");

        Node prev = dummyHead;
        for (int i = 0; i < index; i++)
            prev = prev.next;  // 找到待删除节点的前一个节点
        Node toBeRemoved = prev.next;
        prev.next = toBeRemoved.next;
        toBeRemoved.next = null;
        size--;

        return toBeRemoved.e;
    }

    public E removeFirst() { return removeAtIndex(0); }

    public E removeLast() { return removeAtIndex(size - 1); }

    public void removeElement(E e) {
        dummyHead = removeElement(dummyHead, e);
    }

    private Node removeElement(Node node, E e) {
        if (node == null)
            return null;
        if (node.e != null && node.e.equals(e)) {
            Node next = node.next;
            node.next = null;
            size--;
            return next;
        }
        node.next = removeElement(node.next, e);
        return node;
    }

    public void removeElementNR(E e) {  // 删除任意元素的非递归实现
        Node prev = dummyHead;
        for (int i = 0; i < size; i++) {
            Node curr = prev.next;
            if (curr != null && curr.e.equals(e)) {
                prev.next = curr.next;
                curr.next = null;
                size--;
                return;
            }
            prev = prev.next;
        }
    }

    /*
     * 查操作
     * */
    public boolean contains(E e) {
        return contains(dummyHead.next, e);
    }

    private boolean contains(Node curr, E e) {
        if (curr == null)
            return false;
        if (curr.e.equals(e))
            return true;
        return contains(curr.next, e);
    }
    
    public boolean containsNR(E e) {
        for (Node curr = dummyHead.next; curr != null; curr = curr.next)
            if (curr.e.equals(e))
                return true;
        return false;
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("get failed. Reuqires index < 0 || index > size");
        Node retNode = get(dummyHead.next, index, 0);
        return (E) retNode.e;
    }

    private Node get(Node curr, int index, int depth) {
        if (index == depth)
            return curr;
        return get(curr.next, index, depth + 1);
    }

    public E getNR(int index) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("get failed. Reuqires index < 0 || index > size");

        Node curr = dummyHead.next;
        for (int i = 0; i < index; i++)
            curr = curr.next;
        return curr.e;  // 最后返回的是 node 中的数据
    }

    public E getFirst() { return get(0); }

    public E getLast() { return get(size - 1); }

    /*
     * 改操作
     * */
    // 根据 index 去 set 值在链表中不是常用操作，仅练习用
    public void set(int index, E e) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("set failed. Reuqires index < 0 || index > size");

        Node curr = dummyHead.next;
        for (int i = 0; i < index; i++)
            curr = curr.next;
        curr.e = e;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Size: " + size + ",  ");
        for (Node curr = dummyHead.next; curr != null; curr = curr.next)
            s.append(curr + " -> ");
        s.append("null");
        return s.toString();
    }
}
