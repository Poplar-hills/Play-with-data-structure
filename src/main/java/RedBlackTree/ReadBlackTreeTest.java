package RedBlackTree;

public class ReadBlackTreeTest {
    public static void main(String[] args) {
        RedBlackTree<String, Integer> rbTree = new RedBlackTree<String, Integer>();
        String[] inputKey = {"a", "b", "c", "d", "e", "f", "g"};
        int[] inputVal = { 1,   2,   3,   4,   5,   6,   7 };

        for (int i = 0; i < inputKey.length; i++) {
            rbTree.add(inputKey[i], inputVal[i]);
            System.out.println("Current RedBlackTree size: " + rbTree.getSize());
        }

    }
}
