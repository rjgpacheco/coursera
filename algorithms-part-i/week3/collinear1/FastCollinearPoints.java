/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private static final int MIN_POINTS_PER_SEGMENT = 4;

    private final List<LineSegment> listOfSegments = new ArrayList<LineSegment>();
    private int numberOfSegments = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        if (points == null)
            throw new java.lang.IllegalArgumentException("null argument");

        for (Point point: points) {
            if (point == null)
                throw new java.lang.IllegalArgumentException("null point in input");
        }

        if (areRepeatedPoints(points))
            throw new java.lang.IllegalArgumentException("duplicate points in input");

        int n = points.length;

        int j = 0;
        for (Point p: points)
            System.out.println("i = " + j++ + ": " + p.toString());

        for (int i = 0; i < n; i++) {
            Point pivot = points[i];

            // System.out.println("i = " + i + ": " + "Pivot ~> " + pivot.toString());
            /*
            j = 0;
            for (Point p: points)
                System.out.println("i = " + j++ + ": " + p.toString());
                */


            //Point[] segments = collinearWithPivot(points, i);

            List<LineSegment> segments = collinearWithPivot(points, i);

            if (segments == null)
                continue;



            /*
            int numberCollinear = segments.length;

            if (numberCollinear < MIN_POINTS_PER_SEGMENT)
                continue;

            numberOfSegments++;

            List<LineSegment> segmentsFound = reduceToLineSegment(segments);

            listOfSegments.add();
            */


            numberOfSegments += segments.size();
            listOfSegments.addAll(segments);

            }
        }

    private boolean areRepeatedPoints(Point[] points) {
        int n = points.length;

        Point[] pointsSorted = Arrays.copyOf(points, n);
        Arrays.sort(pointsSorted);

        for (int i = 1; i < n; i++)  {
            if (pointsSorted[i-1].compareTo(pointsSorted[i]) == 0)
                return true;
        }

        return false;
    }

    private LineSegment reduceToLineSegment(Point[] linePoints) {
        Arrays.sort(linePoints);

        return new LineSegment(linePoints[0], linePoints[linePoints.length-1]);
    }


    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] arrayOfSegments = new LineSegment[numberOfSegments];

        for (int i = 0; i < numberOfSegments; i++)
            arrayOfSegments[i] = listOfSegments.get(i);

        return arrayOfSegments;
    }

    private List<LineSegment> collinearWithPivot(Point[] points, int pivotIdx) {

        int n = points.length;

        double[] slopes = new double[n];

        int start = -1;
        int end = -1;

        Point pivot = points[pivotIdx];

        Point[] pointsSorted = Arrays.copyOf(points, points.length);

        Arrays.sort(pointsSorted, pivot.slopeOrder());

        for (int j = 0; j < n; j++)
            slopes[j] = pivot.slopeTo(pointsSorted[j]);

        List<int[]> segmentsFoundIdx = getListOfSegments(slopes);


        if (segmentsFoundIdx.isEmpty())
            return null;

        List<LineSegment> segmentsFound = new ArrayList<>();

        int startIdx;
        int endIdx;
        int count;
        for (int[] found: segmentsFoundIdx) {
            startIdx = found[0];
            endIdx = found[1];
            count = found[2];

            // System.out.println(String.format("start = %d, end = %d, count = %d", start, end, count));

            Point[] collinearPoints = new Point[count+1];
            int i = 0;
            collinearPoints[i++] = pivot;
            for (int j = startIdx; j <= endIdx; j++)
                collinearPoints[i++] = pointsSorted[j];
            Arrays.sort(collinearPoints);

            /*
            for (Point p: collinearPoints)
                System.out.println(p.toString());
                */


            if (pivot.compareTo(collinearPoints[0]) == 0)
                segmentsFound.add(new LineSegment(collinearPoints[0], collinearPoints[count]));
        }

        return segmentsFound;



    }



    private Point[] collinearWithPivotOld(Point[] points, int pivotIdx) {

        int n = points.length;

        List<LineSegment> segmentsFound;

        double[] slopes = new double[n];

        int start = -1;
        int end = -1;

        Point pivot = points[pivotIdx];

        Arrays.sort(points, pivot.slopeOrder());

        /*
        System.out.println(String.format("Pivot ~> %5s", pivot.toString()));
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            System.out.println(String.format("i = %2d ~> %5s, slope = %8.2f", i, p.toString(), pivot.slopeTo(p)));
        }
        */

        for (int j = 0; j < n; j++)
            slopes[j] = pivot.slopeTo(points[j]);

        // Get first sequence of MIN_POINTS_PER_SEGMENT consecutive points
        for (int j = 1; j < n; j++) {
            if (start == -1 &&  (slopes[j] == slopes[j-1]))
                start = j-1;

            if (start != -1 &&  (slopes[j] == slopes[j-1]))
                end = j;

            if (start != -1 &&  (slopes[j] != slopes[j-1])) {
                // we will have end - start + 1 points
                if (end - start + 1 < (MIN_POINTS_PER_SEGMENT-1)) {
                    start = -1;
                    end = -1;
                } else
                    break;
            }
        }

        int numberCollinear = end - start + 1;

        // System.out.println("!");
        if (numberCollinear < (MIN_POINTS_PER_SEGMENT - 1))
            return null;

        // System.out.println("!!");

        Point[] collinearPoints = new Point[numberCollinear+1];
        int i = 0;
        collinearPoints[i++] = pivot;
        for (int j = start; j <= end; j++)
            collinearPoints[i++] = points[j];

        /*
        System.out.println(String.format("start = %d, end = %d, numberCollinear = %d, collinearPoints.length = %d",
                                         start, end, numberCollinear, collinearPoints.length
                                         ));
                                         */

        Arrays.sort(collinearPoints);

        System.out.print(String.format("Pivot ~> %8s :: ", pivot));
        for (int j = 0; j <= numberCollinear; j++)
            System.out.print(String.format("%8s, ", collinearPoints[j].toString()));
        System.out.print("\n");

        if (pivot.compareTo(collinearPoints[0]) == 0)
            System.out.println("!");


        if (pivot.compareTo(collinearPoints[0]) == 0)
            return collinearPoints;


        if (pivot.compareTo(collinearPoints[collinearPoints.length-1]) == 0)
            System.out.println("!!");

        if (pivot.compareTo(collinearPoints[collinearPoints.length-1]) == 0)
            return collinearPoints;


        // System.out.println("!!!");
        return null;

    }


    public static void main(String[] args) {
        // read the n points from a file
        /*
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("Finished!");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        */
        /*
        int range = 1000;

        int y = StdRandom.uniform(range);

        Point[] points = new Point[n*3];

        for (int i = 0; i < n*3; i++)
            points[i] = new Point(i/5, StdRandom.uniform(range));

        FastCollinearPoints collinearPoints = new FastCollinearPoints(points);


        for (Point p: points)
            System.out.println(p.toString());

        for (LineSegment ls: collinearPoints.segments())
            System.out.println(ls.toString());

        */



        List<Point> listOfPoints = new ArrayList<Point>();

        listOfPoints.add(new Point(0, 0));

        listOfPoints.add(new Point(1, 1));
        listOfPoints.add(new Point(2, 2));
        listOfPoints.add(new Point(3, 3));

        listOfPoints.add(new Point(-1, 1));
        listOfPoints.add(new Point(-2, 2));
        listOfPoints.add(new Point(-3, 3));




        // listOfPoints.add(new Point(4, 0));


        /*
        listOfPoints.add(new Point(1, 0));
        listOfPoints.add(new Point(1, 1));
        listOfPoints.add(new Point(1, 2));
        listOfPoints.add(new Point(1, 3));
        listOfPoints.add(new Point(1, 4));

        listOfPoints.add(new Point(2, 0));
        listOfPoints.add(new Point(2, 1));
        listOfPoints.add(new Point(2, 2));
        listOfPoints.add(new Point(2, 3));
        listOfPoints.add(new Point(2, 4));

        listOfPoints.add(new Point(3, 4));
        */

        int numberOfPoints = listOfPoints.size();
        Point[] points = new Point[numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++)
            points[i] = listOfPoints.get(i);

        FastCollinearPoints collinearPoints = new FastCollinearPoints(points);

        LineSegment[] segments = collinearPoints.segments();

        System.out.println(collinearPoints.numberOfSegments);
        for (LineSegment ls: collinearPoints.segments())
            System.out.println(ls.toString());



        /*
        StdDraw.setYscale(-5, 5);
        StdDraw.setXscale(-1, 5);
        StdDraw.setPenRadius(0.025);
        for (Point p: points)
            p.draw();
        StdDraw.show();
        */


        /*
        int[] p = {2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 5, 5};

        int n = p.length;

        int[] groups = new int[n];
        int[] counts = new int[n];
        int j = 0;
        int k = 1;

        List<int[]> lineSegment = new ArrayList<int[]>();
        int start = 0;
        int end = 0;

        for (int i = 1; i < n; i++) {
            if (p[i] != p[i-1]) {
                int[] pair = {j, k, start, end};

                start = i;
                lineSegment.add(pair);
                j++;
                k = 0;
                end = start-1;
            }

            k++;
            end++;
            groups[i] = j;
            counts[i] = k;
        }

        int[] pair = {j, k, start, end};
        lineSegment.add(pair);

        for (int i = 0; i < n; i++)
            System.out.println(String.format("i = %2d: %d ~> %d %d", i, p[i], groups[i], counts[i]));



        for (int[] ls: lineSegment)
            System.out.println(String.format("Group = %d, count = %d, %d ~> %d", ls[0], ls[1], ls[2], ls[3]));

        List<int[]> np = getListOfSegmentsTest(p);
        for (int[] ls: np)
            System.out.println(String.format("%d ~> %d", ls[0], ls[1]));
        */
    }

    public static List<int[]> getListOfSegmentsTest(int[] p) {
        int n = p.length;

        int j = 0;
        int k = 1;

        List<int[]> lineSegment = new ArrayList<int[]>();
        int start = 0;
        int end = 0;

        for (int i = 1; i < n; i++) {
            if (p[i] != p[i-1]) {

                if (k >= 4) {
                    // int[] pair = {j, k, start, end};
                    int[] pair = {start, end};
                    lineSegment.add(pair);
                }


                start = i;

                j++;
                k = 0;
                end = start-1;
            }

            k++;
            end++;
        }

        // (group, count, start, end)
        if (k >= 4) {
            int[] pair = { start, end };
            lineSegment.add(pair);
        }
        return lineSegment;
    }

    public static List<int[]> getListOfSegments(double[] p) {
        int n = p.length;

        //for (double k: p)
        //    System.out.println(k);


        int j = 0;
        int k = 1;

        List<int[]> lineSegment = new ArrayList<int[]>();
        int start = 0;
        int end = 0;

        for (int i = 1; i < n; i++) {
            if (p[i] != p[i-1]) {

                if (k >= 3) {
                    // int[] pair = {j, k, start, end};
                    int[] pair = {start, end, k};
                    lineSegment.add(pair);
                }


                start = i;

                j++;
                k = 0;
                end = start-1;
            }

            k++;
            end++;
        }

        // (group, count, start, end)
        if (k >= 3) {
            int[] pair = { start, end, k};
            lineSegment.add(pair);
        }
        return lineSegment;
    }

}