package SegmentTree.L303_Range_Sum_Query_Immutable;

import SegmentTree.SegmentTree;

public class Solution1 {
    private SegmentTree<Integer> segTree;

    public Solution1(int[] nums) {
        if (nums.length <= 0) return;

        Integer[] arr = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++)
            arr[i] = nums[i];

        segTree = new SegmentTree<Integer>(arr, (a, b) ->  a + b);
    }

    public int sumRange(int i, int j) {
        if (segTree == null)
            throw new IllegalArgumentException("Segment tree is null");
        return segTree.query(i, j);
    }
}