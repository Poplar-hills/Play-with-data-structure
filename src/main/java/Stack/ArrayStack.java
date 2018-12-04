package Stack;

import Array.Array;

/*
 * - 栈是一种先入后出（FILO）的线性数据结构
 * - 主要应用：编辑器的 Undo/Redo 操作、浏览器的前进/后退操作、程序的调用栈、IDE 的括号匹配等等等等
 * - ArrayStack 的复杂度分析：
 *   - void push(E e);     O(n) 均摊
 *   - E pop();            O(1)
 *   - E peek();           O(1)
 *   - int getSize();      O(1)
 *   - boolean isEmpty();  O(1)
 * */

public class ArrayStack<E> implements Stack<E> {
    private Array<E> array;

    public ArrayStack(int capacity) { array = new Array<E>(capacity); }

    public ArrayStack() { this(10); }

    public int getSize() { return array.getSize(); }

    public boolean isEmpty() { return array.isEmpty(); }

    public void push(E e) { array.addLast(e); }

    public E pop() { return array.removeLast(); }

    public E peek() { return array.getLast(); }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d ", getSize()));
        s.append("[");
        for (int i = 0; i < getSize(); i++) {
            s.append(array.get(i));
            if (i != getSize() - 1)
                s.append(", ");
        }
        s.append("] <- top \n");
        return s.toString();
    }
}
