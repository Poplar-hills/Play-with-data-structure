package Array;

public class ArrayTest {
    public static void main(String arg[]) {
        Array<Integer> arr = new Array<Integer>(5);  // 数组中不能用类似 int 的 primitive type，得用对应的包装类
        for (int i = 0; i < 3; i++) {
            arr.addLast(i);
        }
        System.out.print(arr);

        arr.addFirst(-1);
        System.out.print(arr);

        arr.set(0, 10);
        System.out.print(arr);
        System.out.println(arr.get(0));
        System.out.println(arr.findIndex(1));
        System.out.println(arr.contains(5));

        arr.addLast(3);
        arr.addLast(4);  // 此时数组会自动 resize
        System.out.print(arr);

        for (int i = 0; i < 4; i++)
            arr.removeLast();  // 连续移除4个元素，使 size < capacity / 4，检验是否自动 resize 至 capacity / 2
        System.out.print(arr);
    }
}
