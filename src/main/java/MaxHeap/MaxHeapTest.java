package MaxHeap;

import java.util.Arrays;
import java.util.Random;

public class MaxHeapTest {
    public static void main(String[] args) {
        int n = 8;
        int[] randomArr = new int[n];
        MaxHeap<Integer> heap = new MaxHeap<Integer>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            int value = random.nextInt(20);
            heap.insert(value);
            randomArr[i] = value;
        }

        System.out.println("randomArr: " + Arrays.toString(randomArr) + "\n");
        System.out.println("heap written in array: " + heap.toString());

        // 测试 extractMax
        StringBuilder s = new StringBuilder();
        while (heap.getSize() > 0)
            s.append(heap.extractMax() + " ");  // 把数据一个一个放入二叉堆，然后再一个一个取出来，则得到从大到小排序后的数据
        System.out.println("extracted: " + s.toString() + "\n");

        // 测试通过 heapify 构造函数创建最大堆
        Integer[] inputSeq = {15, 17, 19, 15, 22, 16, 28, 30, 41, 62};
        MaxHeap<Integer> heap2 = new MaxHeap<Integer>(inputSeq);  // inputSeq 的类型不能写成 int[]，java 不支持
        System.out.println("inputSeq: " + Arrays.toString(inputSeq));
        System.out.println("heapified: " + heap2.toString());
    }
}
