/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item != null && item.length() > 0) {
                queue.enqueue(item);
            }

        }

        for (int i = 1; i <= k; i++)
            System.out.println(queue.dequeue());
    }
}
