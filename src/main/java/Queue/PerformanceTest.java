package Queue;

import java.util.Random;

/*
* - ArrayQueue 的 dequeue 时间复杂度是 O(n)，因此通过 testQueue 的 for 循环 dequeue 了 n 次之后的总复杂度是 O(n^2)。
* - LoopQueue 的 dequeue 时间复杂度是 O(1)，因此通过 testQueue 的 for 循环 dequeue 了 n 次之后的总复杂度是 O(n)。
* - 对于十万个元素，ArrayQueue 和 LoopQueue 在 enqueue & dequeue 操作的性能上大概相差80倍左右（主要是 dequeue 的区别）。
*   但是要注意性能测试的结果受很多因素影响：不同的机器、不同的系统、不同的 java 版本（JVM 优化）等等。
* 
* - LinkedListQueue 和 LoopQueue 的复杂度在同一级别，所以性能差距不大。
* - 必须明确的是，时间复杂度只衡量趋势，看n无限大时的情况。所以他也有另一个称呼，叫“渐进时间复杂度”，这个“渐进”的由来就在这里，
*   n 要渐进无穷。但是对于具体的测试用例，时间复杂度无法描述具体性能，因为时间复杂度忽略了常数项和低阶项。一个算法，时间需要
*   10000n，100n，2n，其时间复杂度都是O(n)级别的算法。
* - 注意这种性能测试方法不够严谨，更严谨的做法应该是将每一种算法运行多次（如100次）然后取平均值，可信度会更高一些。
* - 另外，不同的 Java 版本对性能也有影响。
 * */

public class PerformanceTest {

    private static double testQueue(Queue q, int opCount) {
        Random random = new Random();
        long startTime = System.nanoTime();

        for (int i = 0; i < opCount; i++)
            q.enqueue(random.nextInt(Integer.MAX_VALUE));

        for (int i = 0; i < opCount; i++)
            q.dequeue();

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;  // 单位转为秒
    }

    public static void main(String[] args) {
        int opCount = 100000;
        ArrayQueue<Integer> arrayQueue = new ArrayQueue<Integer>();
        LoopQueue<Integer> loopQueue = new LoopQueue<Integer>();
        LinkedListQueue<Integer> linkedListQueue = new LinkedListQueue<Integer>();

        double t1 = PerformanceTest.testQueue(arrayQueue, opCount);
        System.out.println("ArrayQueue: " + t1 + " s");

        double t2 = PerformanceTest.testQueue(loopQueue, opCount);
        System.out.println("LoopQueue: " + t2 + " s");

        double t3 = PerformanceTest.testQueue(linkedListQueue, opCount);
        System.out.println("linkedListQueue: " + t3 + " s");
    }
}
