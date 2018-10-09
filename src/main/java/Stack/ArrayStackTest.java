package Stack;

public class ArrayStackTest {
    public static void main(String[] args) {
        Stack<Integer> stack = new ArrayStack<Integer>(5);

        for (int i = 0; i < 5; i++) {
            stack.push(i);
            System.out.print(stack);
        }

        stack.pop();
        System.out.print(stack);
        stack.pop();
        System.out.print(stack);

        System.out.print(stack.peek());
    }
}
