package SegmentTree.L307_Range_Sum_Query_Mutable;

public class Solution2 {
    private int[] data;  // 保持一份原始数组的拷贝
    private int[] sums;  // sums[i] 存储前 i 个元素（0...i-1）之和

    public Solution2(int[] nums) {
        data = new int[nums.length];
        for (int i = 0; i < data.length; i++)
            data[i] = nums[i];

        sums = new int[nums.length + 1];
        sums[0] = 0;
        for (int i = 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + nums[i - 1];
    }

    public int sumRange(int i, int j) {  // O(1) 的复杂度
        if (i < 0 || j < 0 || i > sums.length || j > sums.length || i > j)
            throw new IllegalArgumentException("sumRange failed.");
        return sums[j + 1] - sums[i];
    }

    public void update(int index, int value) {  // 最坏情况下是 O(n) 的复杂度
        if (index < 0 || index >= data.length)
            throw new IllegalArgumentException("udpate failed.");

        data[index] = value;
        for (int i = index + 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + data[i - 1];
    }
}
