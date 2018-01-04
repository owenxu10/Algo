import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (!checkArgument(points))
            throw new java.lang.IllegalArgumentException();

        segments = new ArrayList<>();
        Point[] dummy = points.clone();
        Arrays.sort(dummy);

        for (int p = 0; p < dummy.length; p++) {
            for (int q = p+1; q < dummy.length; q++) {
                for (int r = q+1; r < dummy.length; r++) {
                    for (int s = r+1; s < dummy.length; s++) {
                        if (Double.compare(dummy[p].slopeTo(dummy[q]),
                                dummy[p].slopeTo(dummy[r])) == 0
                                && Double.compare(dummy[p].slopeTo(dummy[q]),
                                dummy[p].slopeTo(dummy[s])) == 0) {
                            LineSegment lineSeg = new LineSegment(dummy[p], dummy[s]);
                            segments.add(lineSeg);
                        }
                    }
                }
            }
        }
    }

    // check the points and sort it
    private boolean checkArgument(Point[] points) {
        // the argument to the constructor is null,
        if (points == null)
            return false;

        // if any point in the array is null,
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                return false;

        // if the argument to the constructor contains a repeated point.
        Point[] dummy = points.clone();
        Arrays.sort(dummy);

        for (int i = 1; i < dummy.length; i++) {
            if (dummy[i].compareTo(dummy[i-1]) == 0)
                return false;
        }

        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
