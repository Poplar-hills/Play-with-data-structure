package Stack;

import java.util.Stack;

public class L20_Valid_Parentheses {

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (this.isOpenningBracket(c)) stack.push(c);
            else if (stack.isEmpty() || !this.match(stack.pop(), c)) return false;
        }

        return stack.isEmpty();
    }

    private boolean isOpenningBracket(Character c) {
        return c == '{' || c == '[' || c == '(';
    }

    private boolean match(Character s1, Character s2) {
        String str = s1.toString() + s2.toString();
        return str.equals("()") || str.equals("[]") || str.equals("{}");
    }
}
