package Map;

public class LinkedListMapTest {
    public static void main(String[] args) {
        LinkedListMap<String, Integer> map = new LinkedListMap<String, Integer>();
        map.add("a", 1);
        System.out.println(map);
        map.add("b", 2);
        System.out.println(map);
        map.add("c", 3);
        System.out.println(map);
        map.add("b", 4);  // update value
        System.out.println(map);

        int a = map.remove("a");
        System.out.println(map + " Removed: " + a);
        System.out.println(map.get("b"));
        System.out.println(map.get("a"));
    }
}
