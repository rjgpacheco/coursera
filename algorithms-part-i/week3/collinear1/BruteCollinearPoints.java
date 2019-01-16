/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> listOfSegments = new ArrayList<LineSegment>();
    private int numberOfSegments = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if (points == null)
            throw new java.lang.IllegalArgumentException("null argument");

        for (Point point: points) {
            if (point == null)
                throw new java.lang.IllegalArgumentException("null point in input");
        }

        if (areRepeatedPoints(points))
            throw new java.lang.IllegalArgumentException("duplicate points in input");

        int n = points.length;

        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                for (int k = j+1; k < n; k++)
                    if (checkIfThreeCollinear(points[i], points[j], points[k]))
                        for (int l = k+1; l < n; l++) {
                            boolean isCollinear = checkIfFourCollinear(points[i], points[j], points[k], points[l]);
                            if (isCollinear) {
                                numberOfSegments++;
                                listOfSegments.add(reduceToLineSegment(points[i], points[j], points[k], points[l]));
                            }
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

    private boolean checkIfFourCollinear(Point p, Point q, Point r, Point s) {
        double s1 = p.slopeTo(q);
        double s2 = p.slopeTo(r);
        double s3 = p.slopeTo(s);

        return (s1 == s2 && s2 == s3);
    }

    private boolean checkIfThreeCollinear(Point p, Point q, Point r) {
        double s1 = p.slopeTo(q);
        double s2 = p.slopeTo(r);

        return (s1 == s2);
    }


    private LineSegment reduceToLineSegment(Point p, Point q, Point r, Point s) {
        Point[] linePoints = new Point[]{p, q, r, s};

        Arrays.sort(linePoints);

        return new LineSegment(linePoints[0], linePoints[3]);
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

    public static void main(String[] args) {

    }
}
