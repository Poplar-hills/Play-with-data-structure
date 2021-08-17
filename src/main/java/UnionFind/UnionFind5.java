package UnionFind;

/*
 * Path Compression Optimization 1
 *
 * - 在 UnionFind4 的基础上加又入了基于路径压缩（方法之一）优化，即通过压缩树的高度来较小 O(h) 中的 h，以达到效率优化的目的
 *   （一种非常经典的优化手段），演示 SEE：https://coding.imooc.com/lesson/207.html#mid=14170（0'00''）。
 *
 * - 思路：在寻找根节点的过程中顺便压缩树的高度，将当前节点链接到其爷爷节点上。
 *
 * - UnionFind5 的时间复杂度近乎是 O(1) 的，是超级快的。因为每一次查询都会进行路径压缩，因此每一个元素都离根节点非常近
 *   （或者说层数非常少），当层数为1的时候，查询的复杂度就是 O(1)。
 * */

public class UnionFind5 implements UF {
    private int[] parents;
    private int[] ranks;  // ranks[i] 表示以 i 为根的集合的树的高度

    public UnionFind5(int size) {
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

    private int find(int p) {                  // 查找元素 p 的集合编号，O(h) 复杂度
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while (parents[p] != p) {
            parents[p] = parents[parents[p]];  // 就添加这一行，将爷爷节点的编号赋给当前的集合编号，即将 p 链接到了其爷爷节点上
            p = parents[p];                    // 跳过 p 原本的父节点，直接从爷爷节点继续遍历
        }
        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public int getSize() { return parents.length; }
}

