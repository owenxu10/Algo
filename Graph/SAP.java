import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > this.G.V() - 1 || w < 0 || w > this.G.V() - 1)
            throw new java.lang.IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.G, w);

        int ancestor = this.ancestor(v, w);
        if (ancestor == -1 || bfsV.distTo(ancestor) == -1 || bfsW.distTo(ancestor) == -1)
            return -1;
        else
            return bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > this.G.V() - 1 || w < 0 || w > this.G.V() - 1)
            throw new java.lang.IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.G, w);

        Queue<Integer> queue = new Queue<Integer>();
        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                queue.enqueue(i);
            }
        }

        int curAncestor = -1;
        int path = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            int tempAncestor = queue.dequeue();
            if (bfsV.distTo(tempAncestor) + bfsW.distTo(tempAncestor) < path) {
                path = bfsV.distTo(tempAncestor) + bfsW.distTo(tempAncestor);
                curAncestor = tempAncestor;
            }
        }

        return curAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (int temp : v) {
            if (temp < 0 || temp > this.G.V() - 1)
                throw new java.lang.IllegalArgumentException();
        }

        for (int temp : w) {
            if (temp < 0 || temp > this.G.V() - 1)
                throw new java.lang.IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.G, w);

        int ancestor = this.ancestor(v, w);

        if (ancestor == -1 || bfsV.distTo(ancestor) == -1 || bfsW.distTo(ancestor) == -1)
            return -1;
        else
            return bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (int temp : v) {
            if (temp < 0 || temp > this.G.V() - 1)
                throw new java.lang.IllegalArgumentException();
        }

        for (int temp : w) {
            if (temp < 0 || temp > this.G.V() - 1)
                throw new java.lang.IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.G, w);

        Queue<Integer> queue = new Queue<Integer>();
        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                queue.enqueue(i);
            }
        }

        int curAncestor = -1;
        int path = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            int tempAncestor = queue.dequeue();
            if (bfsV.distTo(tempAncestor) + bfsW.distTo(tempAncestor) < path) {
                path = bfsV.distTo(tempAncestor) + bfsW.distTo(tempAncestor);
                curAncestor = tempAncestor;
            }
        }

        return curAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
