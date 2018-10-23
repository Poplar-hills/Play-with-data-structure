package MaxHeap;

import java.util.Random;

/*
* - 生成最大堆的两种办法：
*   1. 通过 add 方法将数据一个个添加到空堆中
*   2. 通过 heapify 将一个普通数组整理成最大堆的形态
*
* - 时间复杂度分析：
*   - 第1种方法里，添加1个元素进堆，add 操作的时间复杂度是 O(logn)；因此对于 n 个元素就是 O(nlogn)
*   - 第2种方法里，对于 n 个元素的时间复杂度是 O(n)。（证明比较复杂）
*
* - 通过测试结果可见，在这台计算机上，heapify 的方法要比 add 的方法快近两倍（已经是一个质的飞跃了）。
* */

public class PerformanceTest {
    private static double testHeap(Integer[] testData, boolean isHeapified) {
        double startTime = System.nanoTime();

        MaxHeap<Integer> heap;

        if (isHeapified) {
            heap = new MaxHeap<Integer>(testData);
        } else {
            heap = new MaxHeap<Integer>();
            for (int num : testData)
                heap.add(num);
        }

        double endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;
    }

    public static void main(String[] args) {
        int n = 1000000;
        Integer[] testData = new Integer[n];
        Random random = new Random();

        for (int i = 0; i < n; i++)
            testData[i] = random.nextInt(Integer.MAX_VALUE);

        double t2 = PerformanceTest.testHeap(testData, false);
        System.out.println("Generate heap by adding: " + t2 + " s");

        double t1 = PerformanceTest.testHeap(testData, true);
        System.out.println("Generate heap by heapifing an array: " + t1 + " s");
    }
}
