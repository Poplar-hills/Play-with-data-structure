package UnionFind;

/*
 * - 基于路径压缩的优化
 * - 原因：见 PerformanceTest
 * */

public class UnionFind6 implements UF {
    private int[] setIds;
    private int[] ranks;  // ranks[i] 表示以 i 为根的集合的树的高度

    public UnionFind6(int size) {
        setIds = new int[size];
        ranks = new int[size];
        for (int i = 0; i < size; i++) {
            setIds[i] = i;
            ranks[i] = 1;
        }
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        if (setIds[p] != p)    // 通过递归压缩路径 —— 将 p 和 p 与根节点之间的每一个节点都链接到根节点上
            setIds[p] = find(setIds[p]);
        return setIds[p];
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
            setIds[pRoot] = qRoot;
        else if (ranks[pRoot] > ranks[qRoot])
            setIds[qRoot] = pRoot;
        else {
            setIds[qRoot] = pRoot;
            ranks[pRoot] += 1;
        }
    }

    @Override
    public int getSize() { return setIds.length; }
}

