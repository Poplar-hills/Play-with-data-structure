package Queue;

public class ArrayQueueTest {
    public static void main(String[] args) {
        Queue<Integer> q = new ArrayQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
            System.out.print(q);

            if (i % 3 == 2) {
                q.dequeue();
                System.out.print(q);
            }
        }
    }
}
