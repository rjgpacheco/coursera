/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    static private final double CONFIDENCE_95 = 1.96;

    private final double meanVal;
    private final double stddevVal;
    private final double confidenceLoVal;
    private final double confidenceHiVal;


    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new java.lang.IllegalArgumentException();

        final double [] percolationThresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolationThresholds[i] = runTrial(n);
        }

        meanVal = StdStats.mean(percolationThresholds);
        stddevVal = StdStats.stddev(percolationThresholds);
        confidenceLoVal = meanVal - CONFIDENCE_95*stddevVal / Math.sqrt(trials);
        confidenceHiVal = meanVal + CONFIDENCE_95*stddevVal / Math.sqrt(trials);
    }

    public double mean() {
        return meanVal;
    }

    public double stddev() {
        return stddevVal;
    }

    public double confidenceLo() {
        return confidenceLoVal;
    }

    public double confidenceHi() {
        return confidenceHiVal;
    }

    private double runTrial(int n) {
        Percolation board = new Percolation(n);
        while (!board.percolates()) {
            int row = StdRandom.uniform(n)+1;
            int col = StdRandom.uniform(n)+1;
            board.open(row, col);
        }

        double percolationThreshold = ((double) board.numberOfOpenSites()) / (n*n);
        return percolationThreshold;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());

    }
}
