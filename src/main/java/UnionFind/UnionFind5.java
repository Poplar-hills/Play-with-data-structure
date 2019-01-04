package UnionFind;

/*
 * - 基于路径压缩（其中一个方式）的优化，即通过压缩树的高度来较小 O(h) 中的 h，以达到效率优化的目的（一种非常经典的优化手段）
 * - 原因 SEE：https://coding.imooc.com/lesson/207.html#mid=14170（0'00''）
 * */

public class UnionFind5 implements UF {
    private int[] setIds;
    private int[] ranks;  // ranks[i] 表示以 i 为根的集合的树的高度

    public UnionFind5(int size) {
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

        while (setIds[p] != p) {
            setIds[p] = setIds[setIds[p]];  // 在寻找根节点的过程中顺便压缩树的高度 —— 将 p 的集合编号设置为其爷爷节点的编号，即将 p 链接到了其爷爷节点上
            p = setIds[p];  // 在拿到爷爷节点的编号后再继续循环，从而跳过了 p 原本的父节点，直接从爷爷节点继续遍历
        }
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

