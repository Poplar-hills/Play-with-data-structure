package LinkedList;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();

        // 测试增操作
        for (int i = 0; i < 5; i++) {
            l.addFirst(i);
            System.out.println(l);
        }

        l.addAtIndex(888, 2);
        l.addAtIndex2(999, 4);
        System.out.println(l);

        // 测试查操作
        System.out.println("\nTesting retrieving operations");
        System.out.println(l.contains(888));
        System.out.println(l.get(2));

        // 测试删操作
        System.out.println("\nTesting deleting operations");
        int ret1 = l.removeAtIndex(2);
        System.out.println(l + " Removed: " + ret1);
        int ret2 = l.removeAtIndex(0);
        System.out.println(l + " Removed: " + ret2);
        int ret3 = l.removeFirst();
        System.out.println(l + " Removed: " + ret3);
        int ret4 = l.removeLast();
        System.out.println(l + " Removed: " + ret4);
        l.removeElement(999);
        System.out.println(l);
        l.removeElementNR(2);
        System.out.println(l);

        // 测试通过数组生成链表
        System.out.println("\nTesting constructor");
        LinkedList<Integer> l2 = new LinkedList<Integer>(new Integer[]{9, 8, 7, 6});  // 注意这里数组不能写成 int[]，java 的泛型不支持
        System.out.println(l2);
    }
}
