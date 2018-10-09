package LinkedList;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 5; i++) {
            l.addFirst(i);
            System.out.println(l);
        }

        l.addAtIndex(888, 2);
        System.out.println(l);
        System.out.println(l.contains(888) + " " + l.get(2));

        l.removeAtIndex(2);
        System.out.println(l);
        l.removeFirst();
        l.removeLast();
        System.out.println(l);
    }
}
