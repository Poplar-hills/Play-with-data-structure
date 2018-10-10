package LinkedList.L203_Remove_Linked_List_Elements;

public class ListNode {
    int val;
    ListNode next;

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public ListNode(int val) {
        this(val, null);
    }

    public ListNode getNext() { return next; }  // for unit tests

    public int getVal() { return val; }  // for unit tests
}
