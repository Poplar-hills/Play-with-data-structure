package Map;

import java.util.ArrayList;
import java.util.HashMap;

public class L350_Intersection_of_Two_Arrays_II {
    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> nums1Map = new HashMap<Integer, Integer>();  // 也可以使用 TreeMap
        ArrayList<Integer> intersection = new ArrayList<Integer>();

        for (int num : nums1)
            nums1Map.put(num, nums1Map.containsKey(num) ? nums1Map.get(num) + 1 : 1);

        for (int num : nums2) {
            if (nums1Map.containsKey(num)) {
                intersection.add(num);
                nums1Map.put(num, nums1Map.get(num) - 1);
                if (nums1Map.get(num) == 0)
                    nums1Map.remove(num);
            }
        }

        int[] result = new int[intersection.size()];
        for (int i = 0; i < intersection.size(); i++)
            result[i] = intersection.get(i);
        return result;
    }
}
