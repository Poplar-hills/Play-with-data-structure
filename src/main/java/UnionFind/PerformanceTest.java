package UnionFind;

import java.util.Random;

/*
* - 第1次测试，当 size = 100000；opCount = 10000 时：
*   - UnionFind1: 0.48s
*   - UnionFind2: 0.005s
*
* - 第2次测试，当 size = opCount = 100000 时：
*   - UnionFind1: 7.5s
*   - UnionFind2: 14.8s
*
* - 分析：第2次测试中 UnionFind2 比 UnionFind1 还要慢的原因是：
*   1. UnionFind1 中的 union 操作是用 for 循环遍历数组，这种对同一片内存空间连续访问的操作在 JVM 中被优化的很好；
*      而 UnionFind2 中的 union 虽然没有 for 循环，但是其中的 find 操作是在不同内容地址之间进行跳转，因此会慢一些。
*   2. union 和 isConnected 两个操作在 UnionFind1 和 UnionFind2 中的复杂度分别是：O(n), O(1) 和 O(h), O(h)，
*      如果 size 较大，则有较大的几率产生 h 很大的树，甚至是链表形态的树，因此效率会严重下降。
*
* - 第3版改进思路：针对"有较大的几率产生 h 很大的树"这一点进行改进。当对两个元素 p、q 进行 union 操作时，不再简单地将前者
*   的根节点链接到后者上，而是先判断一下谁所在的树的高度较小（节点数少），然后把高度小的那棵树的根节点链接到高度大的树
*   的根节点上，这样合并形成的树的高度会比较小。
* - 具体实现：见 UnionFind3
* - 测试结果：当 size = opCount = 100000 时：
*   - UnionFind1: 6.17s
*   - UnionFind2: 12.1s
*   - UnionFind3: 0.02s
*
* - 第4版改进思路：第3次改进是通过节点数的多少来判断树的高度（节点数少的则高度小）。但实际可能出现特殊情况，比如以下两棵树：
*          2        4
*       / / \ \     |
*      3 5  9 6     8
*                   |
*                   1
*   其中左树的节点数要比右树的节点少，但是高度却比右树小，此时应该将4链接到2上，而不是反过来。因此可见节点数的多少不一
*   定能反应树高的大小。更好的做法是直接记录数的高度，每次 union 的时候判断树高度，将高度小的合并到大的上去。
* - 具体实现：见 UnionFind4
* - 测试结果：当 size = opCount = 100000 时：
*   - UnionFind3: 0.030723528 s
*   - UnionFind4: 0.019438565 s
*   性能差异不算很大，但是基于 rank 的优化比基于 elCount 的优化更加合理，因此一般并查集的实现都会基于 rank 优化
*
* - 第5版改进思路：即使经过第4版中的优化，并查集中树的高度还是不可避免的会增高，因此可以通过经典的"路径压缩"将树的高度压缩。
*   举例来说，当在对形如图1的树中的节点4做 find 操作时，目的是要找到其根节点0，而并不一定需要遍历中间的所有节点。因此，
*   可以将4移动到3的父节点上，即2上。之后再从2开始继续向上找，将2移动到1的父节点上。这样在完成整个 find 操作之后，整棵
*   树的高度就被降低了，当下次再运行 find 时就会更快：
*               0         0         0
*              /         /         / \
*             1         1         1   2
*            /         /             / \
*           2         2             3  4
*          /         / \
*         3         3  4
*        /
*       4
*   此时我们改变了树的层级结构，应该相应维护一下每个节点的 rank，但实际上并不需要这么早。因为在层级结构之后，树上所有节点
*   的 rank 仍然是从上到下从小到大排列的，只是有可能同一层的两个节点的 rank 可能相同，因此虽然 rank 不再反映准确的层数信息，
*   但是仍能用于判断两个节点谁在上，谁在下（不考虑同级），因此不再需要维护准确的层级信息。
* - 具体实现：见 UnionFind5
* - 测试结果：当 size = opCount = 10000000 时：
*   - UnionFind4: 6.185886673 s
*   - UnionFind5: 4.626142788 s
*   有比较显著的性能提升。但当 size = opCount = 100000 时，反而会比 UnionFind4 慢一点点。
*
* - 第6版改造思路：第5版中每次 find 的时候都将树的高度减小，但是减小的不彻底。最理想的情况是，并查集中的所有树都只有2层，
*   即，将左树通过路径压缩压成右树：
*               0         0
*              /       / / \ \
*             1       1 2  3 4
*            /
*           2
*          /
*         3
*        /
*       4
* - 实现思路：见 UnionFind6
* - 测试结果：当 size = opCount = 10000000 时：
*   - UnionFind4: 8.82 s
*   - UnionFind5: 4.63 s
*   - UnionFind6: 5.48 s
*   可见虽然版本6中队路径压缩的很彻底，但因为递归的开销导致整个数据结构的效率反而不如版本5的效率。而反观第5版，也并不是不能将
*   路径压缩到两层，只是需要对不用节点多调用几次 find 而已（画图理解）。
*
* - 时间复杂度：我们实现的并查集的复杂度是 O(h)，但经过路径压缩优化后这个 h 已经不能反应它和 n 之间的关系了，更严格的结果是
*   O(log*n)，其中 log*n 是递归的 log 函数（iterated logarithm）：
*               / 0   if (n <= 1)
*       log*n =
*               \ 1 + log*(logn)   if (n > 1)
*   O(log*n) 是一个接近 O(1) 级别的复杂度，要比 O(logn) 还要快。
**/

public class PerformanceTest {
    private static double testUF(UF uf, int opCount) {
        int size = uf.getSize();
        Random random = new Random();

        long startTime = System.nanoTime();

        for (int i = 0; i < opCount; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            uf.union(a, b);
        }

        for (int i = 0; i < opCount; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            uf.isConnected(a, b);
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;
    }

    public static void main(String[] args) {
        int size = 100000;
        int opCount = 100000;

        double t1 = PerformanceTest.testUF(new UnionFind1(size), opCount);
        System.out.println("UnionFind1: " + t1 + " s");

        double t2 = PerformanceTest.testUF(new UnionFind2(size), opCount);
        System.out.println("UnionFind2: " + t2 + " s");

        double t3 = PerformanceTest.testUF(new UnionFind3(size), opCount);
        System.out.println("UnionFind3: " + t3 + " s");

        double t4 = PerformanceTest.testUF(new UnionFind4(size), opCount);
        System.out.println("UnionFind4: " + t4 + " s");

        double t5 = PerformanceTest.testUF(new UnionFind5(size), opCount);
        System.out.println("UnionFind5: " + t5 + " s");

        double t6 = PerformanceTest.testUF(new UnionFind6(size), opCount);
        System.out.println("UnionFind6: " + t6 + " s");
    }
}
