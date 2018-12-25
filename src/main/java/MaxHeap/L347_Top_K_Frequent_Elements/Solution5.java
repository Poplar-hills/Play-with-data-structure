package MaxHeap.L347_Top_K_Frequent_Elements;

import java.util.*;

/*
* 比优先队列更快的方法是使用普通数组，将频率作为索引，并在该位置上使用 ArrayList 存储具有该频率的所有元素。
* */

public class Solution5 {
    public List<Integer> topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums)
            map.put(num, map.getOrDefault(num, 0) + 1);

        List<Integer>[] bucket = new List[nums.length + 1];
        for (int key : map.keySet()) {
            int freq = map.get(key);
            if (bucket[freq] == null)
                bucket[freq] = new ArrayList<>();
            bucket[freq].add(key);
        }

        List<Integer> result = new ArrayList<>();
        for (int i = bucket.length - 1; i >= 0 && result.size() < k; i--) {
            if (bucket[i] != null)
                result.addAll(bucket[i]);
        }

        return result;
    }
}
