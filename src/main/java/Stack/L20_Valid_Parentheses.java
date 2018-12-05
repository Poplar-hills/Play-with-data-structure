package Stack;

import java.util.Stack;

public class L20_Valid_Parentheses {

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();

        for (char c : s.toCharArray()) {
            if (this.isOpenningBracket(c))
                stack.push(c);
            else if (stack.isEmpty() || !this.match(stack.pop(), c))
                return false;
        }

        return stack.isEmpty();
    }

    private boolean isOpenningBracket(char c) {
        return c == '{' || c == '[' || c == '(';
    }

    private boolean match(char c1, char c2) {
        return (c1 == '(' && c2 == ')') || (c1 == '[' && c2 == ']') || (c1 == '{' && c2 == '}');
    }
}
