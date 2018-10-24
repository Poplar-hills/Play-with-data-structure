package Queue;

/*
* - 优先队列特点：出队顺序和入队顺序无关，和优先级相关
* - 应用：
*   1. 急诊室就诊 -- 哪个病人的状况紧急就先治疗哪个
*   2. 操作系统任务调度 -- 哪个任务的优先级高就先给哪个任务分配 CPU 资源
*   3. 战略游戏中的 AI -- 一个坦克在多个敌人接近的时候先攻击谁（可以先攻击最强的/最弱的/血最少的/...）
* - 优先队列和排序后的数组的区别：优先队列的排序是动态的，会随时有新的元素加入；数组排序是静态的，排一次就完了。
*
* - 优先队列也是抽象数据结构，其底层实现可以有多种：
*   - 按照确定优先级的时机可以分为两类：
*     1. dequeue 的时候找到最高优先级的元素（可使用普通的动态数组、链表实现）
*     2. enqueue 的时候对元素进行排序（可使用维护顺序的动态数组、链表，或者通过最大堆来实现）
*   - 按照底层实现的数据结构也可以分为两类：
*     1. 线性数据结构：普通的动态数组、普通的链表、维护顺序的动态数组、维护顺序的链表
*     2. 非线性数据结构：最大堆
*
* - 时间复杂度分析：
*                     enqueue    dequeue
*   普通线性数据结构      O(1)      O(n)
*   顺序线性数据结构      O(n)      O(1)
*       最大堆         O(logn)    O(logn)
* */

import MaxHeap.MaxHeap;

public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private MaxHeap<E> maxHeap;

    public PriorityQueue() { maxHeap = new MaxHeap(); }

    public void enqueue(E e) { maxHeap.add(e); }

    public E dequeue() { return maxHeap.extractMax(); }

    public E getFront() { return maxHeap.findMax(); }

    public int getSize() { return maxHeap.getSize(); }

    public boolean isEmpty() { return maxHeap.isEmpty(); }
}
