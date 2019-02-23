/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {

    private int moves;
    private boolean isSolvable;
    private SearchNode solutionNode;

    public Solver(Board initial) {

        MinPQ<SearchNode> queueMain = new MinPQ<SearchNode>();
        MinPQ<SearchNode> queueTwin = new MinPQ<SearchNode>();

        SearchNode minMain;
        SearchNode minTwin;

        // Insert initial board and its twin
        queueMain.insert(new SearchNode(initial, null));
        queueTwin.insert(new SearchNode(initial.twin(), null));

        // int maxIter = 999999;
        // int counter = 0;

        /*
        For each iteration:
            1. Remove smallest from queue
            2. Check if that is the solution
            3. If not, enque its neighbors
        Do this for the board, and its twin. If we solve the twin, tgen we know
        that the board is not solvable.
         */

        while (true) {
            // Just to be safe...
            // System.out.format("Iteration %5d of %5d %n", counter, maxIter);
            /*
            if (maxIter < ++counter) {
                System.out.format("Maximum iteration limit reached!");
                break;
            }
            */


            minMain = queueMain.delMin();
            minTwin = queueTwin.delMin();

            if (minMain.board.isGoal()) {
                // System.out.format("Yay!");
                // System.out.format("moves() = %d%n", minMain.moves);
                this.moves = minMain.moves;
                this.isSolvable = true;
                this.solutionNode = minMain;
                break;
            }

            if (minMain.board.isGoal()) {
                // System.out.format("Aww!");
                // Do stuff...
                this.moves = -1;
                this.isSolvable = false;
                break;
            }

            // Enqueue next iteration
            // if (minMain.prevNode == null || !neighbor.equals(minMain.prevNode.board))
            // ^-- This is to prevent enqueing old solutions --^
            for (Board neighbor : minMain.board.neighbors()) {
                if (minMain.prevNode == null || !neighbor.equals(minMain.prevNode.board))
                    queueMain.insert(new SearchNode(neighbor, minMain));
            }

            for (Board neighbor : minTwin.board.neighbors()) {
                if (minTwin.prevNode == null || !neighbor.equals(minTwin.prevNode.board))
                    queueTwin.insert(new SearchNode(neighbor, minTwin));
            }


        }
    }          // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return this.isSolvable;
    }           // is the initial board solvable?

    public int moves() {
        return this.moves;
    }// min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        Stack<Board> steps = new Stack<Board>();

        SearchNode node = solutionNode;

        steps.push(node.board);
        while (node.prevNode != null) {
            node = node.prevNode;
            steps.push(node.board);
        }

        return steps;
    }     // sequence of boards in a shortest solution; null if unsolvable


    // Change this to private!
    private static class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;
        private int moves;
        private int manhatten;
        private SearchNode prevNode;

        public SearchNode(Board board, SearchNode prevNode) {

            this.board = board;

            if (prevNode == null) {
                this.moves = 0;
            }
            else {
                this.moves = prevNode.moves + 1;
            }

            this.manhatten = board.manhattan();
            this.priority = this.manhatten + this.moves;
            this.prevNode = prevNode;
        }

        public int compareTo(SearchNode that) {
            if (this.board.equals(that.board)) {
                return 0;
            }
            return Integer.compare(this.priority, that.priority);
        }

    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // int dummy = initial.manhattan();

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }


        // My tests

        //StdOut.println(initial.toString());

        /*
        MinPQ<SearchNode> queue = new MinPQ<SearchNode>();
        int k = 0;
        System.out.format("Possible neighbours: %n");
        for (Board b : initial.neighbors()) {
            System.out.format("%n%nNeigbor %d%n", k++);
            System.out.println(b.toString());
            System.out.format("manhattan = %d%n", b.manhattan());
            System.out.format("hamming = %d%n", b.hamming());
        }

        // Insert first
        queue.insert(new SearchNode(initial, null));
        */

    }
}
