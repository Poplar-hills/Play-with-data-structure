package LinkedList.L203_Remove_Linked_List_Elements;

/*
* 用递归解决。
* - 链表天然具有递归性质：一个 1->2->3->4->null 的链表可以看做是头结点 1 后面挂了一个 2->3->4->null 的链表；
*   而 2->3->4 同样可以看做是 2 后面挂了一个 3->4->null 的链表，以此类推。因此，近乎所有和链表相关的操作都可以用递归的形式完成。
* - 以递归的方式思考关键是要识别出：
*   1. 逻辑出口（在哪里停止递归）
*   2. 逻辑的最小单元（重复哪部分操作）
* - 该问题中：
*   1. 递归的逻辑出口是到达 null 节点的时候
*   2. 递归逻辑的最小单元是：如果当前节点是待删除节点，则直接通过 next 跳过当前节点（这样也能达到删除的目的，因为返回的链表中
*      不再有该元素，只不过会有 loitering objects）；如果当前节点不是待删除节点，则不作处理（返回的链表中仍然会有该元素）。
* */

public class Solution3 implements Solution {
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        head.next = removeElements(head.next, val);
        return head.val == val ? head.next: head;
    }
}
