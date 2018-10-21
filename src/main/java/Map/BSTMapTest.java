package Map;

/*
* - 测试以 BST 为底层实现的数据结构时，测试数据不能使排过序的，否则会出现形如链表的 BST。
* - 因为我们在这里要实现的是 Map，而 Map 中比较的是 key，而不是 value（value 可以相同，key 不行），因此在 add 的时候不
*   能按 key 的大小顺序添加。
* - 这里我们构建的 BST 如下：
*         d
*       /  \
*      b    e
*    /  \    \
*   a    c    f
* */

public class BSTMapTest {
    public static void main(String[] args) {
        BSTMap<String, Integer> map = new BSTMap<String, Integer>();
        map.add("d", 4);
        System.out.println(map);
        map.add("b", 2);
        System.out.println(map);
        map.add("e", 5);
        System.out.println(map);
        map.add("c", 3);
        System.out.println(map);
        map.add("a", 1);
        System.out.println(map);
        map.add("f", 6);
        System.out.println(map);

        int ret1 = map.remove("b");
        System.out.println(map + " Removed: " + ret1);
        int ret2 = map.remove("a");
        System.out.println(map + " Removed: " + ret2);
        int ret3 = map.remove("c");
        System.out.println(map + " Removed: " + ret3);
        int ret4 = map.remove("d");
        System.out.println(map + " Removed: " + ret4);

        System.out.println(map.contains("f") + " " + map.contains("a"));
        map.set("f", 1000);
        System.out.println("f: " + map.get("f"));
        System.out.println(map);
    }
}
