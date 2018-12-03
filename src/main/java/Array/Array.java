package Array;

/*
 * Complexity Analysis
 * - 增操作：
 *   - addLast 的复杂度是 O(1)；
 *   - addFirst 是 O(n)；
 *   - addAtIndex 则与 index 取值相关，若 index = 1 则是 O(n)，若 index = size - 1 则是 O(1)；
 *     但平均来说是 O(n/2)，因此还是 O(n) 级别。
 *   - 对于 addLast 来说，它一般是 O(1)，但如果发生 resize，就变成了 O(n)。因此 addLast 最坏情况下是 O(n) 级别的复杂度。
 *     但因为 resize 不是每次增操作都会发生，因此可以将其复杂度均摊到每次增操作中（此时均摊复杂度比最坏情况下的复杂度更有意义）。
 *     均摊之后 addLast 仍然是 O(1)。
 *   - 总的来看，增操作的时间复杂度是 O(n)。
 * - 删操作：同理也是 O(n)
 * - 改操作：已知索引的（如 set、swap）是 O(1)；未知索引的是 O(n)
 * - 查操作：已知索引的（如 get、getFirst）是 O(1)；未知索引的（如 contains, findIndex）是 O(n)
 *
 * */

public class Array<E> {
    private E[] data;
    private int size;

    /*
    * Constructors
    * */
    public Array(int capacity) {
        data = (E[]) new Object[capacity];
        size = 0;
    }

    public Array() {
        this(10);
    }

    public Array(E[] arr) {  // 通过普通数组生成动态数组的构造函数
        data = (E[]) new Object[arr.length];

        for (int i = 0; i < arr.length; i++)
            data[i] = arr[i];

        size = arr.length;
    }

    /*
     * 增操作
     * */
    public void addAtIndex(int index, E e) {
        if (index < 0 || index > size)  // 只有添加元素的时候 index 才可以等于 size（在末尾添加元素）
            throw new IllegalArgumentException("addAtIndex failed. Require index >= 0 and index <= size");

        if (size == getCapacity())
            resize(getCapacity() * 2);

        for(int i = size - 1; i >= index; i--)
            data[i + 1] = data[i];
        data[index] = e;
        size++;
    }

    public void addLast(E e) {
        addAtIndex(size, e);
    }

    public void addFirst(E e) {
        addAtIndex(0, e);
    }

    /*
     * 删操作
     * */
    public E removeAtIndex(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("removeAtIndex failed. Require index >= 0 and index < size");

        E removed = data[index];
        for (int i = index + 1; i < size; i++)
            data[i - 1] = data[i];
        data[size - 1] = null;  // set the loitering object to null
        size--;

        if (size <= getCapacity() / 4 && getCapacity() / 2 != 0)  // shrink the capacity lazily to avoid 复杂度震荡
            resize(getCapacity() / 2);  // 当数组已使用空间只剩 1/4 的时候再缩减数组 capacity（缩减至 1/2)1

        return removed;
    }

    public E removeLast() { return removeAtIndex(size - 1); }

    public E removeFirst() {
        return removeAtIndex(0);
    }

    public void removeElement(E e) {
        int index = findIndex(e);
        if (index != -1)
            removeAtIndex(index);
    }

    /*
     * 改操作
     * */
    public void set(int index, E e) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("set failed. Require index >= 0 and index < size");

        data[index] = e;
    }

    public void swap(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size)
            throw new IllegalArgumentException("swap failed. Index is illegal.");

        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /*
     * 查操作
     * */
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("get failed. Require index >= 0 and index < size");

        return data[index];
    }

    public E getFirst() {
        if (getSize() == 0)
            throw new IllegalArgumentException("getFirst failed. Empty array.");
        return get(0);
    }

    public E getLast() {
        if (getSize() == 0)
            throw new IllegalArgumentException("getLast failed. Empty array.");
        return get(getSize() - 1);
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int findIndex(E e) {
        for (int i = 0; i < size; i++)
            if (data[i].equals(e))
                return i;
        return -1;
    }

    public boolean contains(E e) {
        return findIndex(e) != -1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Size = %d, Capacity = %d\n", size, getCapacity()));
        s.append("[");
        for (int i = 0; i < size; i++) {
            s.append(data[i]);
            if (i != size - 1)
                s.append(", ");
        }
        s.append("]\n");
        return s.toString();
    }

    /*
    * Misc
    * */
    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity];
        for(int i = 0; i < size; i++)
            newData[i] = data[i];
        data = newData;
    }
}
