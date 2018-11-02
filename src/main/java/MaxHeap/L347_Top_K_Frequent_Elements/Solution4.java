package MaxHeap.L347_Top_K_Frequent_Elements;

import java.util.*;

/*
* 相比 Solution3，Solution4 使用了 Comparator 上的 static 方法代替了 lambda
 * */

public class Solution4 {
    public List<Integer> topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums)
            map.put(num, map.getOrDefault(num, 0) + 1);

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(map::get));

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
