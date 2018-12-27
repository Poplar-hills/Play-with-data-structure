package SegmentTree.L303_Range_Sum_Query_Immutable;

public class Solution2 {
    private int[] sums;  // sums[i] 存储前 i 个元素（0...i-1）之和

    public Solution2(int[] nums) {
        sums = new int[nums.length + 1];
        sums[0] = 0;
        for (int i = 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + nums[i - 1];
    }

    public int sumRange(int i, int j) {
        if (i < 0 || j < 0 || i > sums.length || j > sums.length || i > j)
            throw new IllegalArgumentException("sumRange failed.");
        return sums[j + 1] - sums[i];  // 注意是 j + 1
    }
}
