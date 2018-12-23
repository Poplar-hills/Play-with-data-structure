package Set.PerformanceTest;

import Set.Set;
import Set.BSTSet;
import Set.LinkedListSet;
import Set.TrieSet;
import Set.AVLTreeSet;

import java.util.ArrayList;

/*
* - 从结果可见 BSTSet 的效率比 LinkedListSet 高10倍左右
* - 集合的时间复杂度分析：
*               LinkedListSet     BSTSet   BSTSet(平均)  BSTSet(最差)   TrieSet    AVLTreeSet
*   增 add          O(n)           O(h)      O(logn)        O(n)         O(w)      O(logn)
*   查 contains     O(n)           O(h)      O(logn)        O(n)         O(w)      O(logn)
*   删 remove       O(n)           O(h)      O(logn)        O(n)         O(w)      O(logn)
*
* - BSTSet 的三种操作在都是 O(h)，其中 h 为 BST 的最大深度（因为 BST 是二分操作，所以最终访问到的元素只有从根节点到某叶子节点路径
*   上的所有节点）。
* - 需要找出层数 h 和元素个数 n 的关系，才能比较 O(n) 和 O(h)：
*   - 在满二叉树的情况下，1层的 BST 有1个节点，2层的有3个节点，3层的有7个，4层的15个... h 层的 BST 就有 2^h - 1 个。
*     可见 n 和 h 的关系是 n = 2^h - 1 或 h = 以2为底的 log(n+1)。
*   - 这种关系用 big O notation 表示为 O(logn)，其中忽略了系数2，不管以几为底都是一个量级的。因此，上表中对 BST 的三种操作的复杂度
*     也可以写成 O(logn)，但是注意是前提是在平均的情况下。
*   - 而最坏的情况是 BST 退化成链表形态，此时三种操作的复杂度就退化成了 O(n)。因为同样的数据可以创建出不同的 BST，比如同样是
*     1,2,3,4,5,6，如果以4作为根节点，则是一棵正常的 BST；而以1为根节点（即顺序排列），则是一棵形如链表的 BST。因此可见 BST 的时间
*     复杂度很大程度上取决于数据的排列顺序。
*
* - 如果一种算法的时间复杂度是 O(logn)，则它是非常非常快的，介于 O(1) 和 O(O√n) 之间，远比 O(n) 快。很多高级的排序算法是 O(nlogn)。
*   O(n) 和 O(logn) 的比较：SEE: https://coding.imooc.com/lesson/207.html#mid=13705（12'35''）
*
* - 该测试中 TrieSet 比 BSTSet 要快一半，这是因为，对于6530个词的文章来说，O(logn) 大概是13，而 Trie 的时间复杂度
*   跟数据条数无关，只跟要查询的字符串的长度 w 相关，即 O(w)，而大对数英语单词的长度都在10以下。
* - 该测试中 AVLTreeSet 比 TrieSet 还要快一半，因为 AVLTree 是时刻保持平衡，即不会出现 BST 退化成链表的问题。
* */

public class PerformanceTest {

    private static double testSet(Set<String> set) {
        String pathname = "/Users/myjiang/Library/Mobile Documents/com~apple~CloudDocs/Poplar_hills/Dev/java/DataStructure/src/main/java/Set/PerformanceTest/pride-and-prejudice.txt";
        long startTime = System.nanoTime();

        ArrayList<String> words = new ArrayList<String>();
        if (FileOperation.readFile(pathname, words)) {
            System.out.println("Total words: " + words.size());
            for (String word : words)
                set.add(word);  // 只比较 add 操作的效率
            System.out.println("Total different words: " + set.getSize());
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1000000000.0;  // 换算成秒
    }

    public static void main(String[] args) {
        LinkedListSet<String> linkedListSet = new LinkedListSet<String>();
        double time1 = testSet(linkedListSet);
        System.out.println("LinkedListSet: " + time1 + " s\n");

        BSTSet<String> bstSet = new BSTSet<String>();
        double time2 = testSet(bstSet);
        System.out.println("BSTSet: " + time2 + " s\n");

        TrieSet trieSet = new TrieSet();
        double time3 = testSet(trieSet);
        System.out.println("TrieSet: " + time3 + " s\n");

        AVLTreeSet<String> avlTreeSet = new AVLTreeSet<String>();
        double time4 = testSet(avlTreeSet);
        System.out.println("AVLTreeSet: " + time4 + " s");
    }
}
