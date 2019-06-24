package Queue;

/*
* - 优先队列的2个特点：
*   1. 出队顺序和入队顺序无关，仅和优先级相关
*   2. 动态选择优先级最高的任务执行（这也是它与排序后的数组的区别：优先队列的排序是动态的，会随时有新的元素加入；数组排序是静态的，排一次就完了）
*
* - 应用：
*   1. 急诊室就诊 -- 哪个病人的状况紧急就先治疗哪个
*   2. 操作系统任务调度 -- 哪个任务的优先级高就先给哪个任务分配 CPU 资源
*   3. 战略游戏中的 AI -- 一个坦克在多个敌人接近的时候先攻击谁（可以先攻击最强的/最弱的/血最少的/...）
*
* - 实现：
*   - 优先队列实际上就是包装了一下最大堆；
*   - 优先队列与普通队列在实现上的区别在于 enqueue 和 dequeue 这2个方法（不是直接加入/移除即可，而是需要需要根据优先级重新排序）。
*
* - 优先队列也是抽象数据结构，其底层实现可以有多种：
*   - 按照确定优先级的时机可分为两类：
*     1. dequeue 时找到最高优先级的元素（可使用普通动态数组、链表实现）
*     2. enqueue 时对元素进行排序（可使用维护顺序的动态数组、链表，或者通过最大堆来实现）
*   - 按照底层实现的数据结构也可以分为两类：
*     1. 线性数据结构：普通动态数组、普通链表、维护顺序的动态数组、维护顺序的链表
*     2. 非线性数据结构：最大堆
*     注：最大堆也可以使用线性数据结构来实现（SEE: MaxHeap），但仍然属于非线性数据结构的分类中。
*   - 不同的实现时间复杂度不同：
*                    enqueue    dequeue
*      普通线性结构      O(1)      O(n)      - 若用类似动态数组的线性结构来实现，则每次出队的时候都需要遍历找到最大元素
*      顺序线性结构      O(n)      O(1)      - 若还是线性数据结构，但是在入队的时候进行排序，因此数据的顺序性一直在被维护着
*        最大堆       O(logn)    O(logn)    - 与 BST 不同，最大堆是平衡树，最差情况下的复杂度也是 O(logn)，而不是只平均情况下
* */

import MaxHeap.MaxHeap;

public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private MaxHeap<E> maxHeap;

    public PriorityQueue() { maxHeap = new MaxHeap(); }

    public void enqueue(E e) { maxHeap.insert(e); }

    public E dequeue() { return maxHeap.extractMax(); }

    public E getFront() { return maxHeap.findMax(); }

    public int getSize() { return maxHeap.getSize(); }

    public boolean isEmpty() { return maxHeap.isEmpty(); }
}
