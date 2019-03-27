package AVLTree;

// 从量级上来说，AVL tree 和红黑树的效率都是 O(logn)。但从统计上来说，红黑树会比 AVL tree 更高一些。

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
    }
}
