package Map;

/*
* - 经过和 Set 中一样的词频统计测试，BSTMap 的效率是 LinkedListMap 的50倍左右。
* - 时间复杂度分析：
*             LinkedListMap     BSTMap   BSTMap(平均)   BSTMap(最差)
*   add           O(n)           O(h)      O(logn)         O(n)
*   remove        O(n)           O(h)      O(logn)         O(n)
*   set           O(n)           O(h)      O(logn)         O(n)
*   get           O(n)           O(h)      O(logn)         O(n)
*   contains      O(n)           O(h)      O(logn)         O(n)
*  (为什么 BST 的复杂度是 O(h)? h 与 n 的关系是什么? 这两个问题可以自己手动推导一遍)
*
* - 根据 Map 的底层实现分类，Map 也可以分为"有序映射"和"无序映射"（即映射中的 key 是否具有顺序性，是否可以方便地从小到大遍历）。
*   - 有序映射可通过 BST 实现（我们实现的 BSTMap 就是有序映射）
*   - 无序映射可通过哈希表实现（联想 JS 中的普通对象和 Map 的区别）
* */

public class PerformanceTest {
}
