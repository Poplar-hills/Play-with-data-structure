package SegmentTree;
import SegmentTree.L303_Range_Sum_Query_Immutable.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
    @Test
    void should_find_sum_of_elements_between_indices_using_solution1() {
        int[] nums = {-2, 0, 3, -5, 2, -1};
        Solution1 s = new Solution1(nums);

        assertEquals(1, s.sumRange(0, 2));
        assertEquals(-1, s.sumRange(2, 5));
        assertEquals(-3, s.sumRange(0, 5));
    }

    @Test
    void should_find_sum_of_elements_between_indices_using_solution2() {
        int[] nums = {-2, 0, 3, -5, 2, -1};
        Solution2 s = new Solution2(nums);

        assertEquals(1, s.sumRange(0, 2));
        assertEquals(-1, s.sumRange(2, 5));
        assertEquals(-3, s.sumRange(0, 5));
    }
}