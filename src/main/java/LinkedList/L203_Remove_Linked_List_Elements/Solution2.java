package LinkedList.L203_Remove_Linked_List_Elements;

/*
 * Input:  5->1->2->5->3->5->4->5, val = 5
 * Output: 1->2->3->4
 *
 * Solution1 的问题在于，如果节点在链表头处，可以直接删除；而如果节点在链表中间，则需要先获取前一个节点，因此导致需要分支处理
 * 两种情况。而使用虚拟头结点（相当于链表中的所有节点都有了前一个节点）则可以统一两种情况。
 * */

public class Solution2 implements Solution {
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummyHead = new ListNode(-1);
        dummyHead.next = head;

        ListNode prev = dummyHead;
        while (prev.next != null) {
            ListNode curr = prev.next;
            if (curr.val == val) {
                prev.next = curr.next;
                curr.next = null;
            } else
                prev = prev.next;
        }

        return dummyHead.next;
    }
}
