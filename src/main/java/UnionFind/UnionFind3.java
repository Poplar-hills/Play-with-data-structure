package UnionFind;

/*
* - 基于元素个数（element count）的优化
* - 原因：见 PerformanceTest
* */

public class UnionFind3 implements UF {
    private int[] setIds;
    private int[] elCounts;  // elCounts[i] 表示以 i 为根的集合中的元素个数（即以 i 为根的树的高度）

    public UnionFind3(int size) {
        setIds = new int[size];
        elCounts = new int[size];
        for (int i = 0; i < size; i++) {
            setIds[i] = i;
            elCounts[i] = 1;
        }
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while(setIds[p] != p)
            p = setIds[p];
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

        if (elCounts[pRoot] < elCounts[qRoot]) {
            setIds[pRoot] = qRoot;  // 将 elCount 小的树合并到 elCount 大的树上
            elCounts[qRoot] += elCounts[pRoot];
        } else {
            setIds[qRoot] = pRoot;
            elCounts[pRoot] += elCounts[qRoot];
        }
    }

    @Override
    public int getSize() { return setIds.length; }
}

