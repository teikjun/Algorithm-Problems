import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static final int INF = 1000000000;
    static ArrayList<ArrayList<IntPair>> adjList;
    static int[] dist;
    static int vertexCount;
    static int edgeCount;
    static int startVertex;
    static boolean first = true;

    // Idea: Run Bellman Ford algo for V-1 passes, or until no modifications 
    // On the V-th pass, if there is a negative cycle, run BFS and label the reachable nodes as -INF
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        while (true) {
            vertexCount = io.getInt(); // n
            edgeCount = io.getInt(); // m
            int queryCount = io.getInt(); // q
            startVertex = io.getInt(); // s
            if (vertexCount == 0 && edgeCount == 0 && queryCount == 0 && startVertex == 0) {
                break;
            } else if (!first) {
                System.out.println();
            }
            // initialize adjList
            adjList = new ArrayList<ArrayList<IntPair>>();
            for (int i = 0; i < vertexCount; i++) {
                adjList.add(new ArrayList<IntPair>());
            }
            // add edges to adjList
            for (int i = 0; i < edgeCount; i++) {
                int u = io.getInt();
                int v = io.getInt();
                int w = io.getInt();
                addEdge(u, v, w);
            }

            bellmanFord(startVertex);

            // print minimum distance from startVertex to endVertex for each query
            for (int j = 0; j < queryCount; j++) {
                int endVertex = io.getInt();
                int shortestPath = dist[endVertex];
                if (shortestPath == INF) {
                    System.out.println("Impossible");
                } else if (shortestPath == -INF) {
                    System.out.println("-Infinity");
                } else {
                    System.out.println(shortestPath);
                }
            }
            first = false;
        }
        io.close();
    }

    static void addEdge(int u, int v, int w) {
        adjList.get(u).add(new IntPair(v, w));
    }

    // flags for the bellmanFord algo
    static boolean modified = false;
    static boolean hasNegativeCycle = false;

    // bellmanFord algo, BFS if there is negative weight cycle
    static void bellmanFord(int s) {
        // initialize all dist to INF, except for s which has 0 dist
        dist = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            dist[i] = INF;
        }
        dist[s] = 0;
        // Bellman Ford's routine, basically = relax all E edges V-1 times
        for (int i = 0; i < vertexCount - 1; i++) { // for V-1 passes
            modified = false;
            for (int u = 0; u < vertexCount; u++) { // These 2 loops go through all edges
                if (dist[u] != INF) {    // if dist[u] is INF, relaxing edge (u, v) will not improve estimate
                    for (IntPair v_w : adjList.get(u)) {
                        int v = v_w.first();
                        int w = v_w.second();
                        relaxCheckModified(u, v, w);
                    }
                }
            }
            if (!modified) { // If no modifications in a pass, there are no negative cycle 
                return;        // So we can terminate bellmanFord here
            }
        }
        // one more pass to check for negative weight cycle
        // run bfs() on all vertices reachable from negative weight cycle
        // O(V + E) in total since each vertex is visited once. In the worst case, all edges are double checked 
        hasNegativeCycle = false; 
        for (int u = 0; u < vertexCount; u++) { 
            if (dist[u] != INF) {    
                for (IntPair v_w : adjList.get(u)) {
                    int v = v_w.first();
                    int w = v_w.second();
                    relaxCheckNegativeCycle(u, v, w);
                    if (hasNegativeCycle) {
                        bfs(u);
                        // set hasNegativeCycle to false
                        hasNegativeCycle = false;
                        break;
                    }
                }
            }
        }

    }
    // bfs using dist array instead of visited array
    static void bfs(int s) {
        Queue<Integer> queue = new LinkedList<Integer>();
        dist[s] = -INF;
        queue.add(s);
        while (!queue.isEmpty()) {
            Integer curr = queue.remove(); 
            for (IntPair v_w : adjList.get(curr)) {
                int v = v_w.first();
                if (dist[v] != -INF) {
                    dist[v] = -INF;
                    queue.add(v);
                }
            }
        }

    }

    static void relaxCheckModified(int u, int v, int w) {
        if (dist[v] > dist[u] + w) {
            dist[v] = dist[u] + w;
            modified = true;
        }
    }
    static void relaxCheckNegativeCycle(int u, int v, int w) {
        if (dist[v] > dist[u] + w) {
            hasNegativeCycle = true;
        }
    }


}