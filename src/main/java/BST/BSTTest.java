package BST;

import java.util.ArrayList;

/*
*           5
*         /   \
*        2    6
*      /  \    \
*     0    3    8
* */

public class BSTTest {
    public static void main(String[] args) {
        BST<Integer> bst = new BST<Integer>();
        int[] inputSeq = {5,2,6,8,3,8,0};  // 共7个元素，但生成的 BST size = 6，不允许重复元素

        // 测试添加元素
        for (int i : inputSeq) {
            bst.add(i);
            System.out.println("Current BST size: " + bst.getSize());
        }
        System.out.println(bst);

        // 测试遍历 bst
        System.out.println("\nTesting level order traversing...");
        bst.levelOrderTraverse();

        // 测试 getMin, getMax
        System.out.println("\nTesting getMin, getMax...");
        System.out.println(String.format("Min: %d. Max: %d", bst.getMin(), bst.getMax()));

        // 测试 floor
        System.out.println("\nTesting floor...");
        System.out.println(bst.floor(4));
        System.out.println(bst.floor(1));
        System.out.println(bst.floor(7));
        System.out.println(bst.floor(0));
        System.out.println(bst.floor(-1));
        System.out.println(bst.floor(10));

        // 测试 removeMax
        System.out.println("\nTesting removeMax...");
        ArrayList<Integer> nums = new ArrayList<Integer>();
        while (!bst.isEmpty())
            nums.add(bst.removeMax());  // 不断 removeMax 并放到数组中
        System.out.println("Removed: " + nums);  // 结果应该是从大到小排列的
        System.out.println("Current BST size: " + bst.getSize());
        System.out.println(bst);  // 最后 bst 为 null
    }
}
