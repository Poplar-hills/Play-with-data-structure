package LinkedList.L203_Remove_Linked_List_Elements;

/*
* Input:  5->1->2->5->3->5->4->5, val = 5
* Output: 1->2->3->4
*
* 要从链表中删除（多个）节点，需考虑：
* 1. 如果节点在链表头处，则可以直接删除
* 2. 如果节点在链表中间，则需要：
*   a. 先获得待删除节点的前一个节点
*   b. 将前一个节点的 next 指向待删除节点的 next
*   c. 将待删除节点的 next 置为 null
* 3. 因为待删除节点可能有多个，所以要循环起来
* */

public class Solution1 implements Solution {
    public ListNode removeElements(ListNode head, int val) {
        while (head != null && head.val == val) {
            ListNode delNode = head;
            head = delNode.next;
            delNode.next = null;
        }

        if (head == null)
            return null;

        ListNode prev = head;
        while (prev.next != null) {
            ListNode curr = prev.next;
            if (curr.val == val) {
                prev.next = curr.next;
                curr.next = null;
            } else
                prev = prev.next;
        }

        return head;
    }
}
