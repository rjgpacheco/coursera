/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class Board {

    private final int[][] blocks;
    private final int n;

    public Board(int[][] inBlocks) {

        int m = inBlocks[0].length;

        this.blocks = new int[m][m];
        for (int i = 0; i < m; i = i + 1) {
            for (int j = 0; j < m; j = j + 1)
                this.blocks[i][j] = inBlocks[i][j];
        }
        this.n = this.blocks[0].length;
    }          // construct a board from an n-by-n array of blocks

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return n;
    }                // board dimension n

    public int hamming() {
        int outOfPlace = 0;
        int k = 0;
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                k = k + 1;
                if (this.blocks[i][j] != 0 && this.blocks[i][j] != k) {
                    outOfPlace = outOfPlace + 1;
                }
            }
        }

        return outOfPlace;
    }                  // number of blocks out of place

    public int manhattan() {

        int distance = 0;

        int k = 0;
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                k = k + 1;
                // In python:
                // i = ceil(k / n) - 1
                // j = k - i*n - 1

                if (this.blocks[i][j] != 0 && this.blocks[i][j] != k) {
                    int desiredI = (int) Math.ceil(this.blocks[i][j] / (double) n) - 1;
                    int desiredJ = this.blocks[i][j] - desiredI * n - 1;

                    int blockDistance = Math.abs(i - desiredI) + Math.abs(j - desiredJ);

                    distance = distance + blockDistance;
                }
            }
        }

        return distance;
    }                // sum of Manhattan distances between blocks and goal

    public boolean isGoal() { // is this board the goal board?
        int k = 0;
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                // If we make it all the way to the final block, check if its zero
                if (i == (n - 1) && j == (n - 1) && this.blocks[i][j] == 0) {
                    return true;
                }

                // If there is a block out of place, return false
                k = k + 1;
                if (this.blocks[i][j] != k) {
                    return false;
                }
            }
        }
        return false;
    }

    private int[][] swap(int[][] oldBlocks, int firstI, int firstJ, int secondI, int secondJ) {

        int[][] newBlocks = new int[n][n];

        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1)
                newBlocks[i][j] = oldBlocks[i][j];
        }

        newBlocks[secondI][secondJ] = oldBlocks[firstI][firstJ];
        newBlocks[firstI][firstJ] = oldBlocks[secondI][secondJ];

        return newBlocks;
    }

    public Board twin() {

        int firstI = -1;
        int firstJ = -1;

        int secondI = -1;
        int secondJ = -1;

        // First block
        outerloop:
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                if (this.blocks[i][j] != 0) {
                    firstI = i;
                    firstJ = j;
                    break outerloop;
                }
            }
        }

        // Second block
        outerloop:
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                if ((i != firstI || j != firstJ) && this.blocks[i][j] != 0) {
                    secondI = i;
                    secondJ = j;
                    break outerloop;
                }
            }
        }

        // Swap the value
        return new Board(swap(this.blocks, firstI, firstJ, secondI, secondJ));

    }                    // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {

        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;


        if (this.blocks[0].length != that.blocks[0].length) {
            return false;
        }

        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }        // does this board equal y?

    public Iterable<Board> neighbors() {
        // Iterable<Board> iter = new NeighborIterable();
        // return iter;
        Queue<Board> neighbors = new Queue<Board>();

        int zeroI = -1;
        int zeroJ = -1;

        outerloop:
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    break outerloop;
                }
            }
        }

        if (isValidPosition(zeroI - 1, zeroJ)) { // Up
            neighbors.enqueue(new Board(swap(blocks, zeroI, zeroJ, zeroI - 1, zeroJ)));
        }

        if (isValidPosition(zeroI + 1, zeroJ)) { // Down
            neighbors.enqueue(new Board(swap(blocks, zeroI, zeroJ, zeroI + 1, zeroJ)));
        }

        if (isValidPosition(zeroI, zeroJ - 1)) { // Left
            neighbors.enqueue(new Board(swap(blocks, zeroI, zeroJ, zeroI, zeroJ - 1)));
        }

        if (isValidPosition(zeroI, zeroJ + 1)) { // Right
            neighbors.enqueue(new Board(swap(blocks, zeroI, zeroJ, zeroI, zeroJ + 1)));
        }

        return neighbors;


    }     // all neighboring boards


    private boolean isValidPosition(int i, int j) {
        return (i >= 0 && i < n && j >= 0 && j < n);
    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }               // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        String filename = args[0];

        System.out.println("Hello");

        // read in the board specified in the filename
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board initial = new Board(tiles);
        System.out.println(initial.dimension());

        System.out.println(initial.toString());
        System.out.println(initial.isGoal());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());

        System.out.println("Board neighbors:");

        Iterable<Board> neighbors = initial.neighbors();

        for (Board board : neighbors)
            System.out.println(board.toString());
        // initial.NeighborIterator


        System.out.format("manhattan() = %d%n", initial.manhattan());
        tiles[0][0] = 1;
        System.out.format("manhattan() = %d%n", initial.manhattan());


        System.out.format("isGoal:%b%n", initial.isGoal());
    } // unit tests (not graded)

}


