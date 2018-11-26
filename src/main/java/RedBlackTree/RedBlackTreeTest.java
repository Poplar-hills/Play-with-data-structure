package RedBlackTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedBlackTreeTest {
    public static void main(String[] args) {
        int n = 10000;
        Random random = new Random();
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < n; i++)
            list.add(random.nextInt(Integer.MAX_VALUE));

        RedBlackTree<Integer, Integer> rbTree = new RedBlackTree<Integer, Integer>();

        for (Integer x : list)
            rbTree.add(x, null);

        System.out.println("Size: " + rbTree.getSize());
    }
}
