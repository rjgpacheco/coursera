/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PointSET {

    private SET<Point2D> pointSet;

    public PointSET() {
        // construct an empty set of points
        pointSet = new SET<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {
        // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();

        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();

        return pointSet.contains(p);
    }

    public void draw() {
        // draw all points to standard draw

    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();

        if (pointSet.isEmpty()) {
            return null;
        }

        List<Point2D> points = new ArrayList<Point2D>();

        Iterator<Point2D> allPoints = pointSet.iterator();
        while (allPoints.hasNext()) {
            Point2D point = allPoints.next();
            if (rect.contains(point)) {
                points.add(point);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        // Brute force approach, as askek

        if (p == null) throw new IllegalArgumentException();
        
        if (pointSet.isEmpty() || pointSet.size() == 1) {
            return null;
        }

        Point2D closest = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        Iterator<Point2D> allPoints = pointSet.iterator();

        while (allPoints.hasNext()) {
            Point2D point = allPoints.next();
            if (point.equals(p)) {
                continue;
            }

            double distance = p.distanceTo(point);

            if (distance < closestDistance) {
                closest = point;
                closestDistance = distance;
            }
        }

        return closest;
    }

    public static void main(String[] args) {

    }                 // unit testing of the methods (optional)
}
