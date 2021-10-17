package UnionFind;

public class UnionFindTest {
    public static void main(String[] args) {
        UF uf = new UnionFind6(10);
        System.out.println("Size: " + uf.getSize());

        uf.union(4, 8);
        System.out.println(uf.isConnected(8, 4));
        uf.union(8, 2);
        System.out.println(uf.isConnected(2, 4));

        System.out.println(uf.isConnected(2, 0));
        System.out.println(uf.isConnected(1, 6));
    }
}
