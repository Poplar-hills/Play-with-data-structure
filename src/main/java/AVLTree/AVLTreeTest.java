package AVLTree;

import java.util.ArrayList;
import java.util.List;

// AVL tree 的效率已经很高了，但从统计上来说，红黑树会比 AVL tree 更高。

public class AVLTreeTest {
    public static void main(String[] args) {
        AVLTree<Integer> avlTree = new AVLTree<Integer>();
        int[] inputSeq = {0,1,2,3,4,5,6,7,8,9};

        System.out.println("Testing add...");
        for (int n : inputSeq) {
            avlTree.add(n);
            System.out.println("Current AVlTree size: " + avlTree.getSize());
        }
        System.out.println("isBST: " + avlTree.isBST(avlTree.getRoot()));
        System.out.println("isBalanced: " + avlTree.isBalanced());

        System.out.println("\nTesting remove...");
        avlTree.remove(3);
        System.out.println("size: " + avlTree.getSize());
        avlTree.remove(0);
        System.out.println("size: " + avlTree.getSize());
        System.out.println("isBST: " + avlTree.isBST(avlTree.getRoot()));
        System.out.println("isBalanced: " + avlTree.isBalanced());

        System.out.println("\nTesting inOrderTraverse");
        List<Integer> list = new ArrayList<Integer>();
        avlTree.inOrderTraverse(avlTree.root, list);
        System.out.println(list);  // should print out [1, 2, 4, 5, 6, 7, 8, 9]
    }
}
