package Map.PerformanceTest;

import Map.LinkedListMap;
import Map.HashMap;
import Map.BSTMap;
import Map.AVLTreeMap;
import Map.Map;
import Set.PerformanceTest.FileOperation;

import java.util.ArrayList;

/*
 * - 经过和 Set 中一样的词频统计测试，BSTMap 的效率是 LinkedListMap 的50倍左右。
 * - 时间复杂度分析：
 *
 *   LinkedListMap   BSTMap   BSTMap(平均)   BSTMap(最差)   AVLTreeMap   HashMap(红黑树版)(平均)   HashMap(链表)(平均)
 *       O(n)         O(h)      O(logn)         O(n)        O(logn)          O(log(n/M))              O(n/M)
 *
 *  (为什么 BST 的复杂度是 O(h)? h 与 n 的关系是什么? 这两个问题可以自己手动推导一遍)
 *
 * - 根据 Map 的底层实现分类，Map 也可以分为"有序映射"和"无序映射"（即映射中的 key 是否具有顺序性，是否可以方便地从小到大遍历）。
 *   - 有序映射可通过 BST 实现（我们实现的 BSTMap 就是有序映射）
 *   - 无序映射可通过哈希表实现（联想 JS 中的普通对象和 Map 的区别）
 *   - 注："有序性"的解释 SEE: https://coding.imooc.com/learn/questiondetail/81002.html
 *
 * - AVLTreeMap 比 BSTMap 大约快三分之一，因为 AVLTreeMap 通过保持平衡解决了 BSTMap 的可能退化成链表形态的问题，
 *   因此效率更高。具体 SEE: AVLTree.java
 *
 * - HashMap 比 AVLTreeMap 更快，因为 HashMap 用空间换时间 -- 大小为 M 的数组通过哈希值随机访问的复杂度为 O(1)，只有在哈希冲突时
 *   才使用 O(logn) 的红黑树或者 O(n) 的链表，因此平均起来的复杂度是 O(log(n/M)) 或 O(n/M)。注意是"平均"，而最坏的情况是 n 个元
 *   素全部哈希冲突，都挤在数组的同一个位置上，此时哈希表退化成了一棵大红黑树或一个大链表，数组其它位置上都是空，此时的复杂度就退化成了
 *   O(logn) 或 O(n)。因此情况发生的原因不是哈希表设计有问题（如 M 太小）就是数据有问题。M 取值太小的问题可以通过动态扩容的解决，
 *   SEE: HashTable.java
 * */

public class PerformanceTest {
    private static double testMap(Map<String, Integer> map) {
        String pathname = "/Users/myjiang/Library/Mobile Documents/com~apple~CloudDocs/Poplar_hills/Dev/java/DataStructure/src/main/java/Set/PerformanceTest/pride-and-prejudice.txt";
        long startTime = System.nanoTime();

        ArrayList<String> words = new ArrayList<String>();
        if (FileOperation.readFile(pathname, words)) {
            System.out.println("Total words: " + words.size());
            for (String word : words) {
                if (!map.contains(word))
                    map.add(word, 1);  // 只比较 add 操作的效率
                int a = map.get(word) + 1;
                map.set(word, a);
            }
            System.out.println("Total different words: " + map.getSize());
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1000000000.0;
    }

    public static void main(String[] args) {
        LinkedListMap<String, Integer> linkedListMap = new LinkedListMap<String, Integer>();
        double time1 = testMap(linkedListMap);
        System.out.println("LinkedListMap: " + time1 + " s\n");

        BSTMap<String, Integer> bstMap = new BSTMap<String, Integer>();
        double time2 = testMap(bstMap);
        System.out.println("BSTMap: " + time2 + " s\n");

        AVLTreeMap<String, Integer> avlTreeMap = new AVLTreeMap<String, Integer>();
        double time3 = testMap(avlTreeMap);
        System.out.println("AVLTreeMap: " + time3 + " s\n");

        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        double time4 = testMap(hashMap);
        System.out.println("HashMap: " + time4 + " s\n");
    }
}
