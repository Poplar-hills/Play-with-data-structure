package HashTable;

/*
 * - hashCode 方法只用于计算哈希值，但当产生哈希冲突的时候（两个不同对象计算出了相同的哈希值），仍然需要比较两个对象是否相等。比如，
 *   如果要使用 Student 类实例作为 HashSet 的元素或 HashMap 的 key 的话d，光有 hashCode 方法还不够，还需要 equals 方法在产出哈希
 *   冲突的时候比较两个对象是否相同。
 * */

public class Student {
    private int grade;
    private int clazz;
    private String firstname;
    private String lastname;

    public Student(int grade, int clazz, String firstname, String lastname) {
        this.grade = grade;
        this.clazz = clazz;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public int hashCode() {  // 哈希函数
        int B = 31;  //

        int hash = 0;
        hash = hash * B + grade;  // int 型的
        hash = hash * B + clazz;
        hash = hash * B + firstname.toLowerCase().hashCode();  // 字符串型的成员变量可以直接使用 hashCode 方法
        hash = hash * B + lastname.toLowerCase().hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object o) {  // 参数类型必须是 Object，因为要覆盖父类上的 equals 方法
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Student another = (Student) o;
        return this.grade == another.grade &&  // 逐一比较成员变量
                this.clazz == another.clazz &&
                this.firstname.toLowerCase().equals(another.firstname.toLowerCase()) &&
                this.lastname.toLowerCase().equals(another.lastname.toLowerCase());
    }
}
