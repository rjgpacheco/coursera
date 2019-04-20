/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;


public class KdTree {

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean level; // If true, compare on x (aka, split on y)

        public Node(Point2D p, boolean level, RectHV rect) {
            this.p = p;
            this.level = level;
            this.rect = rect;
        }

        public boolean branchToRt(Point2D p) {
            if (this.level) {
                // Compare on x
                if (p.x() >= this.p.x()) { return true;  } // Go to rt
                if (p.x()  < this.p.x()) { return false; } // Go to lb
            }

            if (!this.level) {
                // Compare on y
                if (p.y() >= this.p.y()) { return true;  } // Go to rt
                if (p.y()  < this.p.y()) { return false; } // Go to lb
            }

            return false;
        }
        
        // TODO: Remove later
        public RectHV getRect() {
            return rect;
        }

    }

    private Node root;
    private int size;

    public KdTree() {

        // construct an empty set of points
        this.root = new Node(null, true, new RectHV(0,0,1,1));
        this.size = 0;
    }

    public boolean isEmpty() {
        // is the set empty?
        return root.p == null;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        // tree.insert(p);
        root = insert(root, p, root);
    }

    private Node insert(Node node, Point2D p, Node parentNode) {
        if (node == null) {
            // If we hit this, then we're starting a new sub-tree
            size = size + 1;

            // Rect
            RectHV parentRect = parentNode.rect;

            double xmin = parentRect.xmin();
            double xmax = parentRect.xmax();

            double ymin = parentRect.ymin();
            double ymax = parentRect.ymax();

            if (parentNode.level) {
                // Splitting by X, so override xmin or xmax
                // Right or left?
                if  (parentNode.branchToRt(p)) { xmin = parentNode.p.x(); }
                if (!parentNode.branchToRt(p)) { xmax = parentNode.p.x(); }
            }

            if (!parentNode.level) {
                // Splitting by y, so override xmin or xmax
                // Up or down?
                if  (parentNode.branchToRt(p)) { ymin = parentNode.p.y(); }
                if (!parentNode.branchToRt(p)) { ymax = parentNode.p.y(); }
            }

            return new Node(p, !parentNode.level, new RectHV(xmin, ymin, xmax, ymax));
        }

        if (node.p == null) {
            size = size + 1;
            node.p = p;
            return node;
        }

        if (node.p.equals(p)) {
            return node;
        }

        if ( node.branchToRt(p)) { node.rt = insert(node.rt, p, node); }
        if (!node.branchToRt(p)) { node.lb = insert(node.lb, p, node); }

        return node;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null || node.p == null) { return false; }
        if (node.p.equals(p))  { return true;  }

        if ( node.branchToRt(p)) { return contains(node.rt, p); }
        if (!node.branchToRt(p)) { return contains(node.lb, p); }

        return false;
    }

    /*
    // TODO: Remove later
    public Node get(Point2D p) {
        return get(root, p);
    }

    // TODO: Remove later
    private Node get(Node node, Point2D p) {
        if (node == null || node.p == null) { return null; }
        if (node.p.equals(p))  { return node;  }

        if ( node.branchToRt(p)) { return get(node.rt, p); }
        if (!node.branchToRt(p)) { return get(node.lb, p); }

        return null;
    }
    */

    public void draw() {
        // draw all points to standard draw
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        List<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, List<Point2D> points) {
        // TODO: Optimize recursive calls
        if (node != null && node.p != null) {
            if (rect.contains(node.p)) {
                points.add(node.p);
            }

            range(node.rt, rect, points);
            range(node.lb, rect, points);
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        return null;
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();

        System.out.println(kdtree.isEmpty());

        Point2D p1 = new Point2D(0.50, 0.50);
        Point2D p2 = new Point2D(0.75, 0.50);
        Point2D p3 = new Point2D(0.75, 0.75);



        System.out.println(kdtree.isEmpty());

        kdtree.insert(p1);
        kdtree.insert(p2);
        kdtree.insert(p3);

        RectHV rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Point2D p : kdtree.range(rect)) {
            StdOut.printf("%8.6f %8.6f\n", p.x(), p.y());
        }

        System.out.println(kdtree.size());
        System.out.println(kdtree.contains(new Point2D(0.1, 13)));

        // System.out.println(kdtree.get(p1).getRect());
        // System.out.println(kdtree.get(p2).getRect());
        // System.out.println(kdtree.get(p3).getRect());

    }                 // unit testing of the methods (optional)
}
