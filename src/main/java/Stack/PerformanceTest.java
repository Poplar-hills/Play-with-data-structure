package Stack;

import java.util.Random;

/*
* - 当元素个数在十万级的时候，LinkedListStack 比 ArrayStack 快2倍左右（本机测试）。这是因为 ArrayStack 需要时不时重新分配整个
*   静态数组，再将整个数组复制过去；而链表并不需要。
* - 注意这个结果不是一定的。当元素过多的时候（如一千万），LinkedListStack 中的 new 操作会在某些系统上会比 ArrayStack 更耗时，
*   因为要不断寻找可以开辟空间的地方。
* - 但总的来说，LinkedListStack 和 ArrayStack 的性能差距不大（同一复杂度），不像 ArrayQueue 和 LoopQueue 那样有量级上的差别。
* */

public class PerformanceTest {
    private static double testStack(Stack<Integer> stack, int opCount) {
        Random random = new Random();
        long startTime = System.nanoTime();

        for (int i = 0; i < opCount; i++)
            stack.push(random.nextInt(Integer.MAX_VALUE));

        for (int i = 0; i < opCount; i++)
            stack.pop();

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;  // 单位转为秒
    }

    public static void main(String[] args) {
        int opCount = 100000;
        LinkedListStack<Integer> lStack = new LinkedListStack<Integer>();
        ArrayStack<Integer> aStack = new ArrayStack<Integer>();

        double t1 = PerformanceTest.testStack(lStack, opCount);
        System.out.println("LinkedListStack: " + t1 + " s");

        double t2 = PerformanceTest.testStack(aStack, opCount);
        System.out.println("ArrayStack: " + t2 + " s");

    }
}
