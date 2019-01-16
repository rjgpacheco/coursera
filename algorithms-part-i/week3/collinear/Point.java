/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;


public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        double x0 = (double) this.x;
        double y0 = (double) this.y;
        double x1 = (double) that.x;
        double y1 = (double) that.y;

        if (x0 == x1 && y0 == y1) {
            return Double.NEGATIVE_INFINITY; }

        if (y0 == y1) {
            return +0.0; }

        if (x0 == x1) {
            return Double.POSITIVE_INFINITY; }

        return (y1-y0)/(x1-x0);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */

        if (this.x == that.x && this.y == that.y) { return 0; }
        if (this.y < that.y) { return -1; }
        if (this.y > that.y) { return  1; }
        if (this.x < that.x) { return -1; }
        if (this.x > that.x) { return  1; }
        return Integer.MAX_VALUE;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        // Calculate the slopes from this point
        return new SlopeOrderComparator(this);
    }

    private class SlopeOrderComparator implements Comparator<Point> {

        private Point pivot;

        public SlopeOrderComparator(Point pivot) {
            this.pivot = pivot;
        }

        public int compare(Point p1, Point p2) {

            if (p1 == null || p2 == null) throw new java.lang.NullPointerException();

            double slopeP1 = pivot.slopeTo(p1);
            double slopeP2 = pivot.slopeTo(p2);

            if (slopeP1 <  slopeP2)
                return -1;

            if (slopeP1 >  slopeP2)
                return +1;

            return 0;
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }


    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        // int n = Integer.parseInt(args[0]);

        // testCompareToRandom(Integer.parseInt(args[0]));

        /*
        Point p1 = new Point(0, 0);
        Point p2 = new Point(-5, 10);
        Point p3 = new Point(5, 6);

        List<Point> points = new ArrayList<Point>();
        points.add(p1);
        points.add(p2);
        points.add(p3);

        StdDraw.setScale(-20,20);
        StdDraw.clear();

        StdDraw.setPenRadius(0.025);
        points.forEach(point -> point.draw());

        StdDraw.setPenRadius(0.0005);
        p1.drawTo(p2);

        StdDraw.show();
        */

        /*
        //Test slopes
        int x = StdRandom.uniform(10+1);
        int y = StdRandom.uniform(10+1);

        Point p = new Point(x, y);


        System.out.println(String.format("slopeTo with p = (%2d,%2d)", x, y));
        for (int i = 0; i < n; i++) {
            int newX = StdRandom.uniform(10+1);
            Point p1 = new Point(newX, StdRandom.uniform(10+1));
            Point p2 = new Point(newX, StdRandom.uniform(10+1));

            System.out.println(String.format("%2d: p1 = (%2d, %2d): %10.2f, p2 = (%2d, %2d): %10.2f", i,
                                             p1.x, p1.y, p.slopeTo(p1),
                                             p2.x, p2.y, p.slopeTo(p2)));

        }
        */
        /*
        System.out.println("compareWith");

        List<Point> points = new ArrayList<Point>();

        // Point[] points = new Point[n];

        for (int i = 0; i < n; i++)
            points.add(new Point(StdRandom.uniform(10), StdRandom.uniform(10)));

        Point pivot = points.get(StdRandom.uniform(n));

        System.out.println("Pivot point: " + pivot);
        points.sort(pivot.slopeOrder());
        for (int i = 0; i < (n); i++) {
            Point point = points.get(i);
            System.out.println(String.format("Point %d: %s, slope = %5.2f",
                                             i, point.toString(), pivot.slopeTo(point)));
        }


    */
    }
}
