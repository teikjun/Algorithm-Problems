import java.util.ArrayList;
import java.util.Collections;

public class DrivingRange { 
    /* 
    Vertices: cities, Edges: roads, weights: distance.
    Create an MST using Kruskal's algorithm, record the maximum edge weight taken. 
    The maximum edge weight taken is the minimum range of car.
    */ 

    static ArrayList<IntTriple> edgeList = new ArrayList<>();

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int vertexCount = io.getInt(); 
        int edgeCount = io.getInt();

        int maxWeight = 0; // the max weighted edge so far
        int edgesTaken = 0;
        UnionFindDisjointSet ufds = new UnionFindDisjointSet(vertexCount);
        
        for (int i = 0; i < edgeCount; i++) {
            int u = io.getInt();
            int v = io.getInt();
            int w = io.getInt();
            addEdge(u, v, w);
        }
        // sort the edges by weight, if ties, smaller vertex number, if ties, larger vertex number
        Collections.sort(edgeList);
        
        for (IntTriple edge : edgeList) {
            int u = edge.first();
            int v = edge.second();
            int w = edge.third();
            if (!ufds.isSameSet(u, v)) {
                edgesTaken++;
                maxWeight = Math.max(maxWeight, w);
                ufds.union(u,v);
                //  Optimization 
                if (edgesTaken == vertexCount - 1) {
                    break;
                }
            } 
        }
        boolean isMST = edgesTaken == vertexCount - 1;
        if (isMST) {
            System.out.println("" + maxWeight);
        } else {
            System.out.println("IMPOSSIBLE");
        }
        io.close();
    }

    public static void addEdge(int u, int v, int w) {
        edgeList.add(new IntTriple(u, v, w));
    }
}