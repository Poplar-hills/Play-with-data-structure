package MaxHeap.L347_Top_K_Frequent_Elements;

import java.util.*;

/*
* 相比 Solution1，Solution2 做了如下改进：
* - 使用匿名类来化简代码（省掉了自定义的比较器 FreqComparator 和自定义类 Frequency）
* - PrioriqQueue 中存放的不再是 Frequency 实例，而是带比较的元素本身，只是在匿名类中比较的时候比的是元素的出现频率
* */

public class Solution2 {
    public List<Integer> topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums)
            map.put(num, map.containsKey(num) ? map.get(num) + 1 : 1);

        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {  // 实现 Comparator 的匿名类
            @Override
            public int compare(Integer a, Integer b) { return map.get(a) - map.get(b); }
        });

        for (int key : map.keySet()) {
            if (pq.size() < k)
                pq.add(key);
            else if (map.get(key) > map.get(pq.peek())) {
                pq.remove();
                pq.add(key);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!pq.isEmpty())
            result.add(pq.remove());
        return result;
    }
}
