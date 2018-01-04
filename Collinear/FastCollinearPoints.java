import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (!checkArgument(points))
            throw new java.lang.IllegalArgumentException();

        segments = new ArrayList<>();
        int pointMax = -1, pointMin = -1, count = 1;
        Point[] pointsDup = points.clone();
        Arrays.sort(pointsDup);

        for (int i = 0; i < points.length; i++) {
            Point[] dummy = pointsDup.clone();
            Arrays.sort(dummy, pointsDup[i].slopeOrder());
            for (int j = 1; j < dummy.length; j++) {
                if (Double.compare(dummy[0].slopeTo(dummy[j]),
                        dummy[0].slopeTo(dummy[j-1])) == 0) {
                    if (pointMin == -1) {
                        if (dummy[0].compareTo(dummy[j-1]) > 0) {
                            pointMax = 0;
                            pointMin = j-1;
                        }
                        else {
                            pointMin = 0;
                            pointMax = j-1;
                        }
                    }

                    if (dummy[pointMin].compareTo(dummy[j]) > 0)
                        pointMin = j;

                    if (dummy[pointMax].compareTo(dummy[j]) < 0)
                        pointMax = j;

                    count++;

                }
                else {
                    if (count >= 3 && dummy[0].compareTo(dummy[pointMin]) == 0) {
                        LineSegment lineSeg = new LineSegment(dummy[pointMin], dummy[pointMax]);
                        segments.add(lineSeg);
                    }

                    pointMax = -1;
                    pointMin = -1;
                    count = 1;
                }
            }

            if (count >= 3 && dummy[0].compareTo(dummy[pointMin]) == 0) {
                LineSegment lineSeg = new LineSegment(dummy[pointMin], dummy[pointMax]);
                segments.add(lineSeg);
            }

            pointMax = -1;
            pointMin = -1;
            count = 1;

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

        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.segments().length);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment.toString());
//            segment.draw();
        }
//        StdDraw.show();
    }
}