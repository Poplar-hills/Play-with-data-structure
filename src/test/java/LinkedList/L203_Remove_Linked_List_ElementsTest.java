package LinkedList;

import LinkedList.L203_Remove_Linked_List_Elements.ListNode;
import LinkedList.L203_Remove_Linked_List_Elements.Solution;
import LinkedList.L203_Remove_Linked_List_Elements.Solution1;
import LinkedList.L203_Remove_Linked_List_Elements.Solution2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class L203_Remove_Linked_List_ElementsTest {

    void test(Solution solution) {
        // Input:  5->1->2->5->3->5->4->5, val = 5
        // Output: 1->2->3->4

        ListNode head = new ListNode(5,
                new ListNode(1,
                        new ListNode(2,
                                new ListNode(5,
                                        new ListNode(3,
                                                new ListNode(5,
                                                        new ListNode(4,
                                                                new ListNode(5, null))))))));

        ListNode result = solution.removeElements(head, 5);

        int i = 0;
        int[] outputSeq = {1, 2, 3, 4};
        while (result.getNext() != null) {
            assertEquals(result.getVal(), outputSeq[i]);
            result = result.getNext();
            i++;
        }
    }

    @Test
    void should_remove_elements_from_linked_list_using_solution1() {
        Solution s = new Solution1();
        test(s);
    }

    @Test
    void should_remove_elements_from_linked_list_using_solution2() {
        Solution s = new Solution2();
        test(s);
    }
}