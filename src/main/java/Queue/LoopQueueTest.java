package Queue;

public class LoopQueueTest {
    public static void main(String[] args) {
        Queue<Integer> q = new LoopQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
            System.out.print(String.format("enqueued: %d, ", i));
            System.out.println(q);

            if (i % 3 == 2) {
                int e = q.dequeue();
                System.out.print(String.format("dequeued: %d, ", e));
                System.out.println(q);
            }
        }
    }
}
