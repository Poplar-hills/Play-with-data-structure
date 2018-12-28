package SegmentTree.L303_Range_Sum_Query_Immutable;

/*
* - 这里使用数组，每个位置存储前 i 个元素的和
*     输入数组 nums =    [-2, 0, 3, -5, 2, -1]
*     求和数组 sums = [0, -2, -2, 1, -4, -2, -3]
* - 该方法要比线段树效率还高（因为随机访问，所以效率更高）
* - 本题求的是区间和，如果要求区间最大值，则这个方案就实现不了了，需要使用线段树
* */

public class Solution2 {
    private int[] sums;  // sums[i] 存储前 i 个元素（0...i-1）之和

    public Solution2(int[] nums) {
        sums = new int[nums.length + 1];
        sums[0] = 0;
        for (int i = 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + nums[i - 1];
    }

    public int sumRange(int i, int j) {  // O(1) 的复杂度
        if (i < 0 || j < 0 || i > sums.length || j > sums.length || i > j)
            throw new IllegalArgumentException("sumRange failed.");
        return sums[j + 1] - sums[i];  // 注意是 j + 1
    }
}
