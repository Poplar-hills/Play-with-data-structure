package LinkedList;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 5; i++) {
            l.addFirst(i);
            System.out.println(l);
        }

        l.addAtIndex(888, 2);
        l.addAtIndex2(999, 4);
        System.out.println(l);
        System.out.println(l.contains(888) + " " + l.get(2));

        l.removeAtIndex(2);
        System.out.println(l);
        l.removeFirst();
        l.removeLast();
        System.out.println(l + "\n");

        LinkedList<Integer> l2 = new LinkedList<Integer>(new Integer[]{9, 8, 7, 6});  // 测试通过数组生成链表. 注意这里数组不能写成 int[]
        System.out.println(l2);
    }
}
