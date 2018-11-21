package Map;

public class AVLTreeMapTest {
    public static void main(String[] args) {
        AVLTreeMap<String, Integer> avlTreeMap = new AVLTreeMap<String, Integer>();
        avlTreeMap.add("a", 1);
        avlTreeMap.add("b", 2);
        avlTreeMap.add("c", 3);
        avlTreeMap.add("d", 4);
        avlTreeMap.add("e", 5);
        avlTreeMap.add("e", 6);  // 这里实现的 AVLTree 不允许重复元素
        avlTreeMap.add("f", 7);
        avlTreeMap.add("g", 8);
        System.out.println("size: " + avlTreeMap.getSize());
        System.out.println("isBST: " + avlTreeMap.isBST(avlTreeMap.getRoot()));
        System.out.println("isBalanced: " + avlTreeMap.isBalanced());

        avlTreeMap.remove("b");
        avlTreeMap.remove("d");
        System.out.println("size: " + avlTreeMap.getSize());
        System.out.println("isBST: " + avlTreeMap.isBST(avlTreeMap.getRoot()));
        System.out.println("isBalanced: " + avlTreeMap.isBalanced());
    }
}
