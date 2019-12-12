package UnionFind;

/*
 * - UnionFind3 对 UnionFind2 做了基于树大小的优化。
 * - 原因 SEE: https://coding.imooc.com/lesson/207.html#mid=14168（7'43''）
 * */

public class UnionFind3 implements UF {
    private int[] setIds;
    private int[] sizes;  // sizes[i] 表示以 i 为根的集合中的元素个数（即以 i 为根的树上的元素个数）

    public UnionFind3(int size) {
        setIds = new int[size];
        sizes = new int[size];
        for (int i = 0; i < size; i++) {
            setIds[i] = i;
            sizes[i] = 1;
        }
    }

    private int getSetId(int p) {  // 查找元素 p 的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while(setIds[p] != p)
            p = setIds[p];
        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(h) 复杂度
        return getSetId(p) == getSetId(q);
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(h) 复杂度
        int pRoot = getSetId(p);
        int qRoot = getSetId(q);

        if (pRoot == qRoot) return;

        if (sizes[pRoot] < sizes[qRoot]) {  // 判断 p, q 所在的树的 size，将小者的根节点连接到大者的根节点上（加上该优化后性能大幅提升）
            setIds[pRoot] = qRoot;
            sizes[qRoot] += sizes[pRoot];
        } else {
            setIds[qRoot] = pRoot;
            sizes[pRoot] += sizes[qRoot];
        }
    }

    @Override
    public int getSize() { return setIds.length; }
}

