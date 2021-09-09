package UnionFind;

/*
 * Path Compression Optimization 2
 *
 * - 在 UnionFind4 的基础上加又入了基于路径压缩的另一种优化 —— 使用递归一次性将要查找的节点连接到根节点上（而不是像 UnionFind5
 *   那样每次上移一层），演示 SEE：https://coding.imooc.com/lesson/207.html#mid=14171（0'0''）。
 *
 * - UnionFind6 与 UnionFind5 的性能差距微乎其微，都是近乎为 O(1) 。因为每一次查询都会进行路径压缩，因此每一个元素都离根节点
 *   非常近（或者说层数非常少），当层数为1的时候，查询的复杂度就是 O(1)。
 *
 * - 推荐用该版本作为 UnionFind 的标准实现。
 * */

public class UnionFind6 implements UF {
    private int[] parents;
    private int[] ranks;  // ranks[i] 表示以 i 为根的集合的树的高度

    public UnionFind6(int size) {
        parents = new int[size];
        ranks = new int[size];
        for (int i = 0; i < size; i++) {
            parents[i] = i;
            ranks[i] = 1;
        }
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) return;

        if (ranks[pRoot] < ranks[qRoot])  // 基于 rank 进行 union
            parents[pRoot] = qRoot;
        else if (ranks[pRoot] > ranks[qRoot])
            parents[qRoot] = pRoot;
        else {
            parents[qRoot] = pRoot;
            ranks[pRoot] += 1;
        }
    }

    private int find(int p) {               // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        if (parents[p] != p)                // 通过递归压缩路径，即将 p 和根节点之间的所有节点都直接连接到 p 的根节点上
            parents[p] = find(parents[p]);  // 在压缩过程中，不断将 p 的父节点（parents[p]）连接到 p 的根节点上
        return parents[p];                  // 最后返回 p 的父节点，也就是 p 的根节点
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public int getSize() { return parents.length; }
}

