package Set;

import Array.Array;

import java.util.*;

public class L349_Intersection_of_Two_Arrays {
    public int[] intersection(int[] nums1, int[] nums2) {
        HashSet<Integer> nums1Set = new HashSet<Integer>();
        HashSet<Integer> intersection = new HashSet<Integer>();

        for (int num : nums1)
            nums1Set.add(num);

        for (int num : nums2)
            if (nums1Set.contains(num))
                intersection.add(num);

        int i = 0;
        int[] result = new int[intersection.size()];
        for (int num : intersection)
            result[i++] = num;
        return result;
    }
}
