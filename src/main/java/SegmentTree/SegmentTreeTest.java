package SegmentTree;

/*
* 数组 [-2, 0, 3, -5, 2, -1]，业务逻辑是求和
* 这样生成的线段树的索引形态应该是：
*               [0..5]
*             /      \
*        [0..2]      [3..5]
*        /   \       /    \
*    [0..1]  [2]  [3..4]  [5]
*    /   \        /   \
*  [0]  [1]     [3]  [4]
*
* 根据业务逻辑求值后的最终形态是：
*                 -3         - 数组中所有数字之和。
*              /     \
*            1       -4      - 算出 mid 为2，因此索引0-2上的元素之和为左子树根节点的值，索引3-5上的元素之和为右子树根节点的值。
*          /  \     /  \
*        -2   3   -3   -1    - 左子树 mid 为1，因此索引从0-1的元素之和即为下一层左子树根节点的值，索引2上的值即为下一层右子树根节点的值。
*       /  \     /  \        - 右子树同理
*     -2   0   -5   2        - 最后一层同理
* */

public class SegmentTreeTest {
    public static void main(String[] args) {
        Integer[] inputSeq = {-2, 0, 3, -5, 2, -1};  // 输入必须是 Integer[]，如果是 int[]，则需要手动遍历转换成 Integer[]
        SegmentTree<Integer> segmentTree = new SegmentTree<Integer>(inputSeq, (a, b) -> a + b);  // 第二个参数也可以是 Merger 类的实例，或匿名类

        System.out.println(segmentTree);
        System.out.println(segmentTree.query(0, 2));
        System.out.println(segmentTree.query(0, 3));
        System.out.println(segmentTree.query(2, 5));
        System.out.println(segmentTree.query(0, 5));

        segmentTree.set(3, 5);  // update
        System.out.println(segmentTree);
        System.out.println(segmentTree.query(0, 2));
        System.out.println(segmentTree.query(0, 3));
        System.out.println(segmentTree.query(2, 5));
        System.out.println(segmentTree.query(0, 5));
    }
}
