import java.util.ArrayList;
import java.util.TreeSet;

public class Main {
    // Idea: Run dijkstra's algo and print shortest distance to each endVertex
    public static final int INF = 1000000000;
    static ArrayList<ArrayList<IntPair>> adjList;
    static TreeSet<IntPair> pq;
    static int[] dist;
    static int vertexCount;
    static int edgeCount;
    static int startVertex;
    static boolean first = true;

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
            //  initialize adjList
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

            dijkstra(startVertex);

            // print minimum distance from startVertex to endVertex for each query 
            for (int j = 0; j < queryCount; j++) {
                int endVertex = io.getInt();   
                int shortestPath = dist[endVertex];
                if (shortestPath == INF) {
                    System.out.println("Impossible");
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

    static void dijkstra(int s) {
        // initialize all dist to INF, except for s which has 0 dist
        dist = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            dist[i] = INF;
        }
        dist[s] = 0;
        // initialize pq with all vertices and their shortest path distance
        pq = new TreeSet<IntPair>();
        for (int u = 0; u < vertexCount; u++) {
            pq.add(new IntPair(dist[u], u));
        }
        // main loop
        while (!pq.isEmpty()) {
            IntPair top = pq.pollFirst();
            int u = top.second(); // unvisted vertex with shortest path
            for (IntPair v_w : adjList.get(u)) {
                int v = v_w.first();
                int w = v_w.second();
                relax(u, v, w);
            }
        }
        //System.out.println(Arrays.toString(dist));
    }

    static void relax(int u, int v, int w) {
        if (dist[v] > dist[u] + w) {
            pq.remove(new IntPair(dist[v], v));
            dist[v] = dist[u] + w;
            pq.add(new IntPair(dist[u] + w, v));
        }
    }

}