import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private static final double BORDERENERGY = 1000;
    private int[][] pixels;
    private boolean isTranposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new java.lang.IllegalArgumentException();

        pixels = new int[picture.height()][picture.width()];
        generatePixels(picture);
        isTranposed = false;
    }

    private boolean checkRange(int column, int row) {
        if (row < 0 || row > pixels.length - 1)
            return false;
        if (column < 0 || column > pixels[0].length - 1)
            return false;
        return true;
    }

    /*
     1. check an entry is outside its prescribed range;
     2. check if two adjacent entries differ by more than 1;
    */
    private boolean checkSeam(int[] seam, boolean isVertial) {
        int prev = -1, cur, max;

        if (isVertial)
            max = pixels[0].length;
        else
            max = pixels.length;

        if (seam == null)
            return false;

        if (isVertial)
            if (seam.length != pixels.length || max <= 1)
                return false;
        if (!isVertial)
            if (seam.length != pixels[0].length || max <= 1)
                return false;

        for (int i = 0; i < seam.length; i++) {
            cur = seam[i];

            if (cur < 0 || cur >= max)
                return false;
            if (prev != -1 && Math.abs(cur-prev) > 1)
                return false;
            prev = cur;
        }


        return true;
    }

    // pixel (x + 1, y) and pixel (x − 1, y)
    private double getEnergySquard(int x, int y) {
        Color colorLeft = new Color(pixels[y][x - 1]);
        Color colorRight = new Color(pixels[y][x + 1]);
        Color colorUp = new Color(pixels[y - 1][x]);
        Color colorDown = new Color(pixels[y + 1][x]);

        return getRGB(colorLeft, colorRight) + getRGB(colorUp, colorDown);
    }

    // pixel (x, y + 1) and pixel (x, y - 1)
    private double getRGB(Color first, Color second) {
        double green = first.getGreen() - second.getGreen();
        double blue = first.getBlue() - second.getBlue();
        double red = first.getRed() - second.getRed();

        return green * green + blue * blue + red * red;
    }

    private void generatePixels(Picture picture) {
        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++) {
                pixels[row][col] = picture.get(col, row).getRGB();
            }
        }
    }

    private int getCol(int d) {
        return d % (pixels[0].length);
    }

    private int getRow(int d) {
        return d / (pixels[0].length);
    }

    private int get1D(int col, int row) {
        return row * (pixels[0].length) + col;
    }

    private boolean relax(double[][] distTo, int[][] edgeTo, int from, int to)
    {
        int fromCol = getCol(from);
        int fromRow = getRow(from);
        int toCol = getCol(to);
        int toRow = getRow(to);
        if (distTo[toRow][toCol] > distTo[fromRow][fromCol] + energy(toCol, toRow))
        {
            distTo[toRow][toCol] = distTo[fromRow][fromCol] + energy(toCol, toRow);
            edgeTo[toRow][toCol] = from;
            return true;
        }
        return false;
    }

    private int[] generatePath(int col, int row, int[][] edgeTo) {
        int[] path = new int[pixels.length];

        for (int i = pixels.length - 1; i >= 0; i--) {
            path[i] = col;
            int prev = edgeTo[row][col];
            col = getCol(prev);
            row = getRow(prev);
        }
        return path;
    }

    private void transposeAll() {
        int[][] newColors = new int[pixels[0].length][pixels.length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                newColors[col][row] = pixels[row][col];
            }
        }
        this.pixels = newColors;
    }

    // current picture
    public Picture picture() {
        Picture pic =  new Picture(pixels[0].length, pixels.length);

        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                pic.set(col, row, new Color(pixels[row][col]));
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return pixels[0].length;
    }

    // height of current picture
    public int height() {
        return pixels.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!checkRange(x, y))
            throw new java.lang.IllegalArgumentException();

        if (x == 0 || y == 0 || x == pixels[0].length-1 || y == pixels.length-1)
            return BORDERENERGY;
        else {
            return Math.sqrt(getEnergySquard(x, y));
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[pixels.length][pixels[0].length];
        int[][] edgeTo = new int[pixels.length][pixels[0].length];

        // init distTo
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                if (row == 0) {
                    distTo[row][col] = BORDERENERGY;
                    edgeTo[row][col] = get1D(col, row);
                }
                else {
                    distTo[row][col] = Double.POSITIVE_INFINITY;
                    edgeTo[row][col] = -1;
                }
            }
        }

        // generate the distTo and edgeTo
        for (int row = 1; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                // col -1
                if (col > 0)
                    relax(distTo, edgeTo, get1D(col - 1, row - 1), get1D(col, row));
                // col
                relax(distTo, edgeTo, get1D(col, row - 1), get1D(col, row));
                // col + 1
                if (col < pixels[0].length - 1)
                    relax(distTo, edgeTo, get1D(col + 1, row - 1), get1D(col, row));
            }
        }

        // find the minimum distTo
        int row = pixels.length - 1;
        double min = Double.POSITIVE_INFINITY;
        int minCol = 0;
        for (int col = 0; col < pixels[0].length; col++) {
            if (distTo[row][col] < min) {
                min = distTo[row][col];
                minCol = col;
            }
        }

        return generatePath(minCol, row, edgeTo);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[][] prevColors = this.pixels;
        transposeAll();
        int[] path = findVerticalSeam();
        this.pixels = prevColors;
        return path;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!isTranposed && !checkSeam(seam, true))
            throw new java.lang.IllegalArgumentException();

        int[][] newPixels =
                new int[pixels.length][pixels[0].length-1];

        for (int row = 0; row < pixels.length; row++) {
            // remove the seam in pixel
            System.arraycopy(pixels[row], 0, newPixels[row], 0, seam[row]);
            System.arraycopy(pixels[row], seam[row]+1, newPixels[row], seam[row], pixels[0].length - 1 - seam[row]);
        }

        pixels = newPixels;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!checkSeam(seam, false))
            throw new java.lang.IllegalArgumentException();

        transposeAll();
        isTranposed = true;
        removeVerticalSeam(seam);
        transposeAll();
        isTranposed = false;
    }

}