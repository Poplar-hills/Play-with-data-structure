package MaxHeap.L347_Top_K_Frequent_Elements;

import java.util.*;

/*
* 相比 Solution2，Solution3:
* - 使用了 lambda 代替了匿名类
* - 使用了 map.getOrDefault
* */

public class Solution3 {
    public List<Integer> topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums)
            map.put(num, map.getOrDefault(num, 0) + 1);

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> map.get(a) - map.get(b));

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
