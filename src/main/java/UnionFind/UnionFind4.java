package UnionFind;

/*
* - 基于 rank 的优化（看树的高度，而不是树中元素个数）
* - 原因 SEE：https://coding.imooc.com/lesson/207.html#mid=14169（0'00''）
* */

public class UnionFind4 implements UF {
    private int[] parents;
    private int[] ranks;  // ranks[i] 表示以 i 为根的集合的树的高度（不再是元素个数）

    public UnionFind4(int size) {
        parents = new int[size];
        ranks = new int[size];
        for (int i = 0; i < size; i++) {
            parents[i] = i;
            ranks[i] = 1;
        }
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= parents.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while(parents[p] != p)
            p = parents[p];
        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;

        if (ranks[pRoot] < ranks[qRoot])
            parents[pRoot] = qRoot;  // 将 rank 小的树合并到 rank 大的树上，合并之后 rank 大的树的高度不变。
        else if (ranks[pRoot] > ranks[qRoot])
            parents[qRoot] = pRoot;
        else {  // 只有当双方的 rank 相等时，合并产生的新树的高度会比之前增加1（画一画就知道了）
            parents[qRoot] = pRoot;
            ranks[pRoot] += 1;
        }
    }

    @Override
    public int getSize() { return parents.length; }
}

