package SegmentTree.L307_Range_Sum_Query_Mutable;

import SegmentTree.SegmentTree;

/*
* 该题目与 L304 号题目的区别就是要多实现一个 update 方法，用于更新区间里的某一个值，并且要求复杂度小于 O(n)。
* 如果采用类似 L304 中 Solution2 的基于数组的方案，则复杂度会是 O(n)（SEE: Solution2），只有采用线段树才能保持 O(logn) 的复杂度。
* */

public class Solution1 {
    private SegmentTree<Integer> segTree;

    public Solution1(int[] nums) {
        if (nums.length <= 0) return;

        Integer[] arr = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++)
            arr[i] = nums[i];

        segTree = new SegmentTree<Integer>(arr, (a, b) ->  a + b);
    }

    public int sumRange(int i, int j) {  // O(logn) 的复杂度
        if (segTree == null)
            throw new IllegalArgumentException("Segment tree is null");
        return segTree.query(i, j);
    }

    public void update(int index, int value) {  // O(logn) 的复杂度
        if (segTree == null)
            throw new IllegalArgumentException("Segment tree is null");
        segTree.set(index, value);
    }
}