package HashTable;

public class HashTableTest {
    public static void main(String[] args) {
        // 使用定义在 Object 上的 hashCode 方法
        int a = 42;
        System.out.println("a:" + ((Integer)a).hashCode());
        int b = -42;
        System.out.println("b:" + ((Integer)b).hashCode());
        double c = 4.345;
        System.out.println("c:" + ((Double)c).hashCode());
        String d = "apple";
        System.out.println("d:" + d.hashCode() + "\n");

        // 下面计算两个自定义类实例的哈希值。结果可见，s1 和 s2 的哈希值是一样的。但如果删除 Student 中的 hashCode 方法，则 Java
        // 会使用默认的 hashCode 方法，即对象的内存地址，这样 s1 和 s2 虽然内容相同，但算出来的哈希值就不一样了。
        Student s1 = new Student(1, 2, "Tom", "Hayse");
        System.out.println("s1: " + s1.hashCode());

        Student s2 = new Student(1, 2, "TOM", "Hayse");
        System.out.println("s2: " + s2.hashCode() + "\n");

        // 测试自己写的 HashTable
        HashTable<String, Integer> ht = new HashTable<String, Integer>();
        ht.add("a", 1);
        System.out.println("HashTable size: " + ht.getSize());
        ht.add("b", 2);
        System.out.println("HashTable size: " + ht.getSize());
        ht.remove("a");
        System.out.println("HashTable size: " + ht.getSize());
    }
}
