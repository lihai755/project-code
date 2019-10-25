package leetcode.algorithms;

import org.junit.Test;

public class AddTwoNumbers_2 {

	@Test
	public void testAddTwoNumbers_2() {
		ListNode l1 = new ListNode(1234);
		ListNode l2 = new ListNode(1234);
		ListNode l3 = addTwoNumbers(l1, l2);
	}

	public class ListNode {
		int val;
		ListNode next;
		ListNode(int x) {
			val = x;
		}
	}
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode c1 = l1;
        ListNode c2 = l2;
        ListNode sentinel = new ListNode(0);
        ListNode d = sentinel;
        int sum = 0;
        while (c1 != null || c2 != null) {
            sum /= 10;
            if (c1 != null) {
                sum += c1.val;
                c1 = c1.next;
            }
            if (c2 != null) {
                sum += c2.val;
                c2 = c2.next;
            }
            d.next = new ListNode(sum % 10);
            d = d.next;
        }
        if (sum / 10 == 1)
            d.next = new ListNode(1);
        return sentinel.next;
    }
}