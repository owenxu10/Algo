import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int num;
    private int openNodes;
    private final boolean[][] isNodeOpen;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufNoBot;

    /**
     * create n-by-n grid, with all sites blocked
     */
    public Percolation(int n) {
        if (n < 1)
            throw new java.lang.IllegalArgumentException();

        num = n;
        openNodes = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNoBot = new WeightedQuickUnionUF(n * n + 1);

        isNodeOpen = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isNodeOpen[i][j] = false;
            }
            uf.union(0, xyTo1D(1, i + 1));
            ufNoBot.union(0, xyTo1D(1, i + 1));
            uf.union(xyTo1D(n, i + 1), n * n + 1);
        }
    }

    /**
     * open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        checkArgs(row, col);

        if (isOpen(row, col))
            return;

        isNodeOpen[row - 1][col - 1] = true;

        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            ufNoBot.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }

        if (row < num && isOpen(row + 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            ufNoBot.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }

        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            ufNoBot.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }

        if (col < num && isOpen(row, col + 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            ufNoBot.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
        openNodes++;
    }

    /**
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        checkArgs(row, col);
        return isNodeOpen[row - 1][col - 1];
    }

    /**
     * is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        checkArgs(row, col);
        return ufNoBot.connected(0, this.xyTo1D(row, col)) && isOpen(row, col);
    }

    /**
     * number of open sites
     */
    public int numberOfOpenSites() {
        return openNodes;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        if (num == 1)
            return isOpen(1, 1);
        return uf.connected(0, num * num + 1);
    }

    private int xyTo1D(int row, int col) {
        checkArgs(row, col);
        return (row - 1) * num + (col - 1);
    }

    private void checkArgs(int row, int col) {
        if (row > num || row < 1)
            throw new java.lang.IllegalArgumentException();

        if (col > num || col < 1)
            throw new java.lang.IllegalArgumentException();
    }

    /**
     * test client (optional)
     */
//    public static void main(String[] args) {
//    }
}
