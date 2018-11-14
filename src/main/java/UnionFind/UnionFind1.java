package UnionFind;

/*
* - 并查集（Union Find）的主要应用：
*   1. 数学中集合类的实现
*   2. 可以非常快速地回答网络中两个节点的连接问题（节点 A 是否与节点 B 相连）
* - 而网络可以用于非常多的场景：
*   1. 社交网络的团关系分析
*   2. 信息网络（如亚马逊的商品、豆瓣的图书、网易云音乐的专辑都可以是节点，再以某种形式定义边，最终形成一个巨大的网络）
*   3. 交通系统（公路、航线、铁路）
*   4. 路由器
*   ....
* - 网络中的连接问题 vs. 路径问题：路径问题要比连接问题包含更多的信息，因此：
*   - 能回答路径问题的算法和数据结构肯定可以回答连接问题
*   - 但是复杂度往往会更高（因为对额外信息进行了计算）
*   可见：
*     - 要高效地回答每个特定问题，就不要计算额外的信息
*     - 在没有场景的情况下讨论算法效率就是耍流氓
*   另一个典型的例子：堆 vs. 顺序列表。堆能回答的问题（取出最大的元素）用顺序列表也都能回答，而且顺序表不仅能取出最大的元素，
*   还能取出第二大、第三大、第k大，但这些信息都是在应用堆结构的场景中不关心的问题。因此计算了这些信息使得顺序表的复杂度更高。
*
* - 在 UnionFind1 这个实现，是使用数组模拟：
*   1  2  3  4  5    union(2, 4)   1  2  3  4  5    union(4, 5)   1  2  3  4  5     isConnected(2, 5)
*   -------------       --->       -------------       --->       -------------          --->         true
*   1  2  3  4  5                  1  4  3  4  5                  1  5  3  5  5
* */

public class UnionFind1 implements UF {
    private int[] setIds;

    public UnionFind1(int size) {
        setIds = new int[size];
        for (int i = 0; i < size; i++)  // 初始化时给每一个元素一个不同的集合编号，即有多少个元素就有多少个集合
            setIds[i] = i;
    }

    private int find(int p) {  // 查找元素 p 所对应的集合编号，O(1) 复杂度
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");
        return setIds[p];
    }

    @Override
    public boolean isConnected(int p, int q) {  // 查看两个元素是否属于同一个集合，O(1) 复杂度
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {  // 合并两个元素所属的集合，O(n) 复杂度
        int pSetId = find(p);
        int qSetId = find(q);

        if (pSetId == qSetId)
            return;

        for (int i = 0; i < setIds.length; i++)
            if (setIds[i] == qSetId)
                setIds[i] = pSetId;
    }

    @Override
    public int getSize() { return setIds.length; }
}
