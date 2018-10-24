package MaxHeap;

import LinkedList.LinkedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/*
* 此题符合"在 N 个元素中选出前 M 个元素"的套路
* - 如果是"在 N 个元素中选出最大的元素"，则只需要排序一次即可；而对于"前 M 个"的需求可以通过归并排序、快速排序算法
*   在 NlogN 的时间里完成。最后再取出前 M 个元素即可。
* - 而更好的方法是使用优先队列，可以在 NlogM 的时间里完成。
* - 具体来说，先在 N 个元素中取 M 个，之后每次再取一个新的元素时，如果新元素比队列中最小的元素大，则把最小的元素扔出
*   去，用新元素取而代之。这样相当于，在遍历过程中优先队列中一直维护着最大的 M 个元素。
* - 从中可见，需要的是一个每次能够 dequeue 最小值的优先队列，因此底层可以使用最小堆实现。而另一方面，"最小"、"最大"
*   都是相对的，我们可以重新定义"小"与"大"。如果将"数值最小"定义为"最优先"，则可以直接使用最大堆实现优先队列。
* - 在本题中，元素的"频率"越小，则优先级越高，越早被 dequeue。因此我们可以定义一个私有类来辅助元素的比较。
* - 注意：Java 的 PriorityQueue 是一个最小堆
* */

public class L347_Top_K_Frequent_Elements {
    private class Frequency implements Comparable<Frequency> {
        int e, frequency;

        public Frequency(int e, int frequency) {
            this.e = e;  // 注意，私有类中的 this 不能少，否则引用的是父类中的属性
            this.frequency = frequency;
        }

        public int compareTo(Frequency f) {
            return this.frequency > f.frequency ? 1 : (this.frequency < f.frequency ? -1 : 0);
        }
    }

    public List<Integer> topKFrequent(int[] nums, int k) {
        // 为每个元素建立"元素-频率"的映射关系
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int num : nums)
            map.put(num, map.containsKey(num) ? map.get(num) + 1 : 1);

        // 使用优先队列维护频率最高的 k 个元素
        PriorityQueue<Frequency> pq = new PriorityQueue<Frequency>();  // Java 的 PriorityQueue 是一个最小堆
        for (int key : map.keySet()) {
            if (pq.size() < k)
                pq.add(new Frequency(key, map.get(key)));
            else if (map.get(key) > pq.peek().frequency) {
                pq.remove();
                pq.add(new Frequency(key, map.get(key)));
            }
        }

        // 当遍历完成后，获得队列中的前 k 个元素
        List<Integer> result = new ArrayList<Integer>();
        while (!pq.isEmpty())
            result.add(pq.remove().e);
        return result;
    }
}
