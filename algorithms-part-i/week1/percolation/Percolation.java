/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */



import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private final WeightedQuickUnionUF unionStructure;
    private final WeightedQuickUnionUF unionStructureNoBot;

    private final boolean[][] grid;

    private final int gridSize;

    private final int virtualTopSite;
    private final int virtualBotSite;

    private int numberOfOpenSitesAcc = 0;

    public Percolation(int n) {

        if (n < 1) throw new java.lang.IllegalArgumentException();

        gridSize = n;
        grid = new boolean[gridSize][gridSize];

        unionStructure = new WeightedQuickUnionUF(gridSize * gridSize + 2);
        unionStructureNoBot  = new WeightedQuickUnionUF(gridSize * gridSize + 1);

        virtualTopSite = 0;
        virtualBotSite = gridSize*gridSize + 1;
    }

    public void open(int row, int col) {
        checkIllegalArgument(row, col);

        if (!isOpen(row, col)) {
            openCell(row, col);

            if (row == 1) {
                unionStructure.union(virtualTopSite, xyTo1D(row, col));
                unionStructureNoBot.union(virtualTopSite, xyTo1D(row, col));
            }

            if (row == gridSize)
                unionStructure.union(virtualBotSite, xyTo1D(row, col));

            connectToOpenNeighbour(row, col);
        }
    }

    public boolean isOpen(int row, int col) {
        checkIllegalArgument(row, col);
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IndexOutOfBoundsException("row or col out of bounds");
        return grid[row-1][col-1];
    }

    public boolean isFull(int row, int col) {
        checkIllegalArgument(row, col);
        return unionStructureNoBot.connected(virtualTopSite, xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        return numberOfOpenSitesAcc;
    }

    public boolean percolates() {
        return unionStructure.connected(virtualBotSite, virtualTopSite);
    }

    public static void main(String[] args) {
        System.out.println("Percolation main");
    }

    private int xyTo1D(int row, int col) {
        return (col - 1) * gridSize + row; // Check!
    }

    private void openCell(int row, int col) {
        numberOfOpenSitesAcc++;
        // row and col are 1-indexed
        grid[row-1][col-1] = true;
    }

    private void connectToOpenNeighbour(int row, int col) {
        connectIfOpen(row, col, row+0, col-1);
        connectIfOpen(row, col, row+0, col+1);
        connectIfOpen(row, col, row-1, col+0);
        connectIfOpen(row, col, row+1, col+0);
    }

    private void checkIllegalArgument(int row, int col) {
        if (!checkIfInGrid(row, col)) throw new
                java.lang.IllegalArgumentException();
    }

    private boolean checkIfInGrid(int row, int col) {
        if (row < 1 || col < 1  || row > gridSize || col > gridSize){
            return false;
        }
        else
            return true;
    }

    private void connectIfOpen(int row, int col, int nRow, int nCol) {
        if (checkIfInGrid(nRow, nCol) && isOpen(nRow, nCol)) {
            unionStructure.union(xyTo1D(row, col), xyTo1D(nRow, nCol));
            unionStructureNoBot.union(xyTo1D(row, col), xyTo1D(nRow, nCol));
        }
    }
}
