/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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

        for (int i = 0; i < n; i++) {
            Point pivot = points[i];

            List<LineSegment> segments = collinearWithPivot(points, i);

            if (segments == null)
                continue;

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

        for (int[] found: segmentsFoundIdx) {
            int startIdx = found[0];
            int endIdx = found[1];
            int count = found[2];

            Point[] collinearPoints = new Point[count+1];
            int i = 0;
            collinearPoints[i++] = pivot;
            for (int j = startIdx; j <= endIdx; j++)
                collinearPoints[i++] = pointsSorted[j];
            Arrays.sort(collinearPoints);

            if (pivot.compareTo(collinearPoints[0]) == 0)
                segmentsFound.add(new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length-1]));
        }

        return segmentsFound;
    }


    public static void main(String[] args) {

    }


    private static List<int[]> getListOfSegments(double[] p) {
        int n = p.length;

        int j = 0;
        int k = 1;

        List<int[]> lineSegment = new ArrayList<int[]>();
        int start = 0;
        int end = 0;

        for (int i = 1; i < n; i++) {
            if (p[i] != p[i-1]) {

                if (k >= MIN_POINTS_PER_SEGMENT-1) {
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
        if (k >= MIN_POINTS_PER_SEGMENT-1) {
            int[] pair = { start, end, k};
            lineSegment.add(pair);
        }
        return lineSegment;
    }

}