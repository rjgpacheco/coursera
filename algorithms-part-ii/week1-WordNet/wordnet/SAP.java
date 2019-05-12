import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 ) throw new java.lang.IllegalArgumentException();
        return getShortestPathLength(v, w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 ) throw new java.lang.IllegalArgumentException();
        return getShortestAncestor(v, w);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException();

        Set<Integer> vSafe = new HashSet<>();
        Set<Integer> wSafe = new HashSet<>();

        for (Integer i: v) {
            if (i == null) throw new java.lang.IllegalArgumentException();
            vSafe.add(i);
        }


        for (Integer i: w) {
            if (i == null) throw new java.lang.IllegalArgumentException();
            wSafe.add(i);
        }

        return getShortestPathLength(vSafe, wSafe);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException();

        Set<Integer> vSafe = new HashSet<>();
        Set<Integer> wSafe = new HashSet<>();

        for (Integer i: v) {
            if (i == null) throw new java.lang.IllegalArgumentException();
            vSafe.add(i);
        }


        for (Integer i: w) {
            if (i == null) throw new java.lang.IllegalArgumentException();
            wSafe.add(i);
        }

        return getShortestAncestor(vSafe, wSafe);
    }

    // Aux stuff ------------------------------------------------------------------------------------

    private Set<Integer> getCommonNodes(int v, int w) {
        Set<Integer> reachableCommon = reachableFrom(v);
        reachableCommon.retainAll(reachableFrom(w));
        return reachableCommon;
    }

    private Set<Integer> getCommonNodes(Iterable<Integer> v, Iterable<Integer> w) {
        Set<Integer> reachableCommon = reachableFrom(v);
        reachableCommon.retainAll(reachableFrom(w));
        return reachableCommon;
    }


    private Integer getShortestAncestor(Integer v, Integer w) {
        BreadthFirstDirectedPaths dfsGv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths dfsGw = new BreadthFirstDirectedPaths(G, w);

        int minLength = Integer.MAX_VALUE;
        int minNode = -1;

        int length;
        Set<Integer> common = getCommonNodes(v, w);

        if (common.isEmpty()) {
            return -1;
        }

        for (int x: common) {
            length = 0;
            for (int i: dfsGv.pathTo(x)) length++;
            for (int i: dfsGw.pathTo(x)) length++;
            if (length < minLength) {
                minLength = length;
                minNode = x;
            }
        }
        return minNode;
    }

    private Integer getShortestAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths dfsGv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths dfsGw = new BreadthFirstDirectedPaths(G, w);

        int minLength = Integer.MAX_VALUE;
        int minNode = 0;

        int length;

        Set<Integer> common = getCommonNodes(v, w);

        if (common.isEmpty()) {
            return -1;
        }

        for (int x: common) {
            length = 0;
            for (int i: dfsGv.pathTo(x)) length++;
            for (int i: dfsGw.pathTo(x)) length++;
            if (length < minLength) {
                minLength = length;
                minNode = x;
            }
        }
        return minNode;
    }


    /*
    private String printPathTo(Iterable<Integer> pathTo) {
        List<Integer> path = new ArrayList<Integer>();
        for (int x: pathTo) {
            path.add(x);
        }

        Integer[] path2 = new Integer[path.size()];
        for (int i = 0; i < path.size(); i++) {
            path2[i] = path.get(i);
        }

        return Arrays.toString(path2).replaceAll("\\[|\\]|,", "").replaceAll("\\s", "-");
    }

     */

    private String printPath(Integer[] path) {
        return Arrays.toString(path).replaceAll("\\[|\\]|,", "").replaceAll("\\s", "-");
    }


    private Set<Integer> reachableFrom(Integer v) {
        BreadthFirstDirectedPaths localG = new BreadthFirstDirectedPaths(G, v);
        Set<Integer> reachable = new HashSet<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (localG.hasPathTo(i)) {
                reachable.add(i);
            }
        }
        return reachable;
    }

    private Set<Integer> reachableFrom(Iterable<Integer> v) {
        BreadthFirstDirectedPaths localG = new BreadthFirstDirectedPaths(G, v);
        Set<Integer> reachable = new HashSet<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (localG.hasPathTo(i)) {
                reachable.add(i);
            }
        }
        return reachable;
    }

    private int getShortestPathLength(Integer v, Integer w) {
        BreadthFirstDirectedPaths dfsGv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths dfsGw = new BreadthFirstDirectedPaths(G, w);

        int minNode = getShortestAncestor(v, w);

        if (minNode == -1) {
            return -1;
        }

        Integer[] shortestPath = knitPaths(dfsGv.pathTo(minNode), dfsGw.pathTo(minNode));
        return shortestPath.length - 1;
    }

    private int getShortestPathLength(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths dfsGv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths dfsGw = new BreadthFirstDirectedPaths(G, w);

        int minNode = getShortestAncestor(v, w);

        if (minNode == -1) {
            return -1;
        }

        Integer[] shortestPath = knitPaths(dfsGv.pathTo(minNode), dfsGw.pathTo(minNode));
        return shortestPath.length - 1;
    }


    private Integer[] knitPaths(Iterable<Integer> vPathTo, Iterable<Integer> wPathTo) {
        List<Integer> vPathTo2 = new ArrayList<Integer>();
        List<Integer> wPathTo2 = new ArrayList<Integer>();

        for (int x: vPathTo) { vPathTo2.add(x); }
        for (int x: wPathTo) { wPathTo2.add(x); }

        wPathTo2.remove(wPathTo2.size() - 1); // Last element is repeated
        Collections.reverse(wPathTo2);

        Integer[] path = new Integer[vPathTo2.size() + wPathTo2.size()];

        int i = 0;
        for (int x : vPathTo2) { path[i++] = x; }
        for (int x : wPathTo2) { path[i++] = x; }

        return path;
    }

    // Aux stuff ------------------------------------------------------------------------------------


    // do unit testing of this class
    public static void main(String[] args) {
        /*
        // java-algs4 SAP digraph1.txt
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        System.out.println(G);

        // System.out.println(sap.reachableFrom(16));

         */

        /*
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        */

        /*
        List<Integer> a = new ArrayList<>(Arrays.asList(13, 23, 24));
        List<Integer> b = new ArrayList<>(Arrays.asList(6, 16, 17));

        sap.getShortestPath(a, b);

        System.out.println(sap.printPath(sap.getShortestPath(a, b)));

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            sap.getShortestPath(v, w);
        }

        /*
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            System.out.println(sap.reachableFrom(v));
        }

         */


        // DepthFirstDirectedPaths J = new DepthFirstDirectedPaths()

    }
}