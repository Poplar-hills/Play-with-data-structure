package Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L349_Intersection_of_Two_ArraysTest {
    @Test
    void should_get_intersection_of_two_array1() {
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};
        L349_Intersection_of_Two_Arrays r = new L349_Intersection_of_Two_Arrays();
        int[] result = r.intersection(nums1, nums2);
        assertArrayEquals(result, new int[]{2});
    }

    @Test
    void should_get_intersection_of_two_array2() {
        int[] nums1 = {4, 9, 5};
        int[] nums2 = {9, 4, 9, 8, 4};
        L349_Intersection_of_Two_Arrays r = new L349_Intersection_of_Two_Arrays();
        int[] result = r.intersection(nums1, nums2);
        assertArrayEquals(result, new int[]{4, 9});
    }
}