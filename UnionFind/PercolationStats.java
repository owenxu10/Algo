import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double CONFIDENCE = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    /**
     * perform trials independent experiments on an n-by-n grid
     */
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new java.lang.IllegalArgumentException();

        double[] threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            double time = 0;
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                if (perc.isOpen(row, col))
                    continue;
                perc.open(row, col);
                time++;
            }
            threshold[i] = time/(n * n);
        }

        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        confidenceLo = mean - CONFIDENCE * stddev / Math.sqrt(trials);
        confidenceHi = mean + CONFIDENCE * stddev / Math.sqrt(trials);
    }

    /**
     * sample mean of percolation threshold
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return stddev;
    }

    /**
     * low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return confidenceLo;
    }

    /**
     * high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return confidenceHi;
    }

    /**
     * test client (described below)
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percStat = new PercolationStats(n, t);
        StdOut.println("mean   = "  + percStat.mean());
        StdOut.println("stddev = "  + percStat.stddev());
        StdOut.println(percStat.confidenceLo() + "," + percStat.confidenceHi());
    }
}
