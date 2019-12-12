package UnionFind;

/*
 * - UnionFind6 采用了基于路径压缩的另一种优化思路 —— 使用递归一次性将要查找的节点链接到根节点上（而不是像 UnionFind5 那样
 *   每次上移一层），演示 SEE：https://coding.imooc.com/lesson/207.html#mid=14171（0'0''）。
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

    private int getSetId(int p) {  // 查找元素 p 所对应的集合编号，O(h) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        if (setIds[p] != p)    // 通过递归压缩路径 —— 将 p 和 p 与根节点之间的每一个节点都链接到根节点上
            setIds[p] = getSetId(setIds[p]);  // 将当前节点链接到父节点的根节点上（每次递归都去找父节点的根节点）
        return setIds[p];
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

