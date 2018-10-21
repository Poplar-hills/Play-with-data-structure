package Set.PerformanceTest;

import Set.Set;
import Set.BSTSet;
import Set.LinkedListSet;

import java.util.ArrayList;

/*
* - 从结果可见 BSTSet 的效率比 LinkedListSet 高10倍左右
* - 集合的时间复杂度分析：
*               LinkedListSet     BSTSet   BSTSet(平均)  BSTSet(最差)
*   增 add          O(n)           O(h)      O(logn)        O(n)
*   查 contains     O(n)           O(h)      O(logn)        O(n)
*   删 remove       O(n)           O(h)      O(logn)        O(n)
*
*  - 三种操作在 BST 实现中的时间复杂度都是 O(h)，其中 h 为 BST 的最大深度（因为 BST 是二分操作，所以最终访问到的元素只有
*    从根节点到某叶子节点路径上的所有节点）
*  - 知道了两种实现的复杂度分别是 O(n) 和 O(h)，那就需要找出 n 和 h 的关系才能比较：
*    - 在满二叉树的情况下，第0层1个节点，第1层2个，第2层4个，第3层8个... 第 h-1 层 2^(h-1) 个，因此一棵 h 层的满二叉树
*      共有 2^0 + 2^1 + 2^2 + 2^3 + ... + 2^(h-1) = 2^h - 1 个节点（等比数列求和）。所以如果 n 和 h 的关系就是 n = 2^h - 1，
*      或 h = 以2为底的 log(n+1)。这种关系用 big O notation 表示为 O(logn)，（忽略系数2，不管以几为底都是一个量级的）。
*      因此，上表中对 BST 的三种操作的复杂度也可以写成 O(logn)，但是注意是前提是在平均的情况下。
*    - 在最坏的情况下，即形如链表的 BST 中，三种操作的复杂度就退化成了 O(n)。因为同样的数据可以创建出不同的 BST，比如同样是
*      1,2,3,4,5,6，如果以4作为根节点，则是一棵通常看到的 BST；而以1为根节点（即顺序排列），则是一棵形如链表的 BST。因此 BST
*      的时间复杂度很大程度上取决于数据的排列顺序。
*  - 如果一种算法的时间复杂度是 O(logn)，则它是非常非常快的，介于 O(1) 和 O(O√n) 之间，远比 O(n) 快。很多高级的排序算法是 O(nlogn)。
* https://coding.imooc.com/lesson/207.html#mid=13705
* */

public class PerformanceTest {

    private static double testSet(Set<String> set) {
        String pathname = "/Users/myjiang/Library/Mobile Documents/com~apple~CloudDocs/Poplar_hills/Dev/java/DataStructure/src/main/java/Set/PerformanceTest/pride-and-prejudice.txt";
        long startTime = System.nanoTime();

        ArrayList<String> words = new ArrayList<String>();
        if (FileOperation.readFile(pathname, words)) {
            System.out.println("Total words: " + words.size());
            for (String word : words)
                set.add(word);
            System.out.println("Total different words: " + set.getSize());
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1000000000.0;  // 换算成秒
    }

    public static void main(String[] args) {
        BSTSet<String> bstSet = new BSTSet<String>();
        double time1 = testSet(bstSet);
        System.out.println("BST Set: " + time1 + " s\n");

        LinkedListSet<String> linkedListSet = new LinkedListSet<String>();
        double time2 = testSet(linkedListSet);
        System.out.println("Linked List Set: " + time2 + " s");

    }
}
