import java.util.ArrayList;
import java.util.Collections;

public class Kruskal {
    static ArrayList<IntTriple> edgeList;

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        while (true) {
            int vertexCount = io.getInt();
            int edgeCount = io.getInt();
            
            int mstWeight = 0;
            int edgesTaken = 0;
            ArrayList<IntTriple> mstEdgeList = new ArrayList<IntTriple>(); 
            UnionFindDisjointSet ufds = new UnionFindDisjointSet(vertexCount);
            edgeList = new ArrayList<IntTriple>();
            
            if (vertexCount == 0 && edgeCount == 0) {
                break;
            }

            // add all edges to the edgeList
            for (int i = 0; i < edgeCount; i++) {
                int u = io.getInt();
                int v = io.getInt();
                int w = io.getInt();
                if (u < v) {
                    addEdge(u, v, w, edgeList); 
                } else {
                    addEdge(v, u, w, edgeList);
                }
            }

            // sort the edges by weight, if ties, smaller vertex number, if ties, larger vertex number
            Collections.sort(edgeList, new WeightComparator());
            
            // System.out.println(edgeList.toString());
            // for each edge in edgeList, 
            // check if taking edge with endpoints u, v will cause a cycle using isSameSet(u, v)
            // if no cycle, greedily take this edge and call union(u, v)
            for (IntTriple edge : edgeList) {
                int u = edge.first();
                int v = edge.second();
                int w = edge.third();
                if (!ufds.isSameSet(u, v)) {
                    // take the edge, update ufds, mstWeight, edgesTaken
                    addEdge(u, v, w, mstEdgeList);
                    ufds.union(u, v);
                    mstWeight += w;
                    edgesTaken++;
                    // Optimization
                    if (edgesTaken == vertexCount - 1) {
                        break;
                    }
                } 
            }
            // the resulting graph is not an MST if any vertex is not visited
            boolean isMST = edgesTaken == vertexCount - 1;
            // print impossible if resulting graph is not an MST, otherwise print mstWeight and each mstEdge
            if (isMST) {
                System.out.println(mstWeight);
                Collections.sort(mstEdgeList, new VertexComparator());
                for (IntTriple mstEdge : mstEdgeList) {
                    System.out.println(mstEdge);
                }
            } else {
                System.out.println("Impossible");
            }
        }       
        io.close();
    }

    public static void addEdge(int u, int v, int w, ArrayList<IntTriple> myList) {
        myList.add(new IntTriple(u, v, w));
    }
    
}