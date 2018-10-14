package BST;

public class BSTTest {
    public static void main(String[] args) {
        BST<Integer> bst = new BST<Integer>();
        int[] inputSeq = {5,2,6,8,3,8,0};
        for (int i : inputSeq)
            bst.add(i);
        System.out.println(bst);
    }
}
