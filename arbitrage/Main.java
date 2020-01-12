import java.util.HashMap;
import java.util.ArrayList;

public class Main {
    // Idea: Construct a graph currencies as vertices, log(b/a) as edge weight.
    // Due to this bijection, the problem is transformed into finding negative weight cycle
    // in a graph of V vertices, not necessarily connected
    public static final double INF = 1000000000;
    static boolean modified = false;
    static boolean hasNegativeCycle = false;
    static double[] dist;
    static int vertexCount;

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);

        while (true) {
            vertexCount = io.getInt(); // C
            if (vertexCount == 0) {
                break;
            }
            ArrayList<ArrayList<Pair>> adjList = new ArrayList<ArrayList<Pair>>();
            // Create a hashmap of currencyString as key, vertex number as value.
            HashMap<String, Integer> currencyHM = new HashMap<>();
            for (int i = 0; i < vertexCount; i++) {
                String currencyString = io.getWord();
                currencyHM.put(currencyString, i);
                adjList.add(new ArrayList<Pair>());;
            }
            // Add edges to adjList
            int edgeCount = io.getInt(); // R
            for (int i = 0; i < edgeCount; i++) {
                String firstCurrency = io.getWord();
                String secondCurrency = io.getWord();
                String exchange = io.getWord();
                String[] exchangeArray = exchange.split(":");
                int a = Integer.parseInt(exchangeArray[0]);
                int b = Integer.parseInt(exchangeArray[1]);
                double weight = -Math.log((double) b / a);
                addEdge(adjList, currencyHM.get(firstCurrency), currencyHM.get(secondCurrency), weight);
            }
            bellmanFordAll(adjList);
            if (hasNegativeCycle) {
                System.out.println("Arbitrage");
            } else {
                System.out.println("Ok");
            }
        }
        io.close();
    }

    static void addEdge(ArrayList<ArrayList<Pair>> adjList, int u, int v, double w) {
        adjList.get(u).add(new Pair(v, w));
    }

    
    // run V passes of bellmanFord for each vertex
    static void bellmanFordAll(ArrayList<ArrayList<Pair>> adjList) {
        hasNegativeCycle = false;
        for (int k = 0; k < vertexCount; k++) {
            // initialize all dist to INF, except for vertex k which has 0 dist
            dist = new double[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                dist[i] = INF;
            }
            dist[k] = (double) 0;
            // Bellman Ford's routine, basically = relax all E edges V-1 times
            for (int i = 0; i < vertexCount - 1; i++) {
                modified = false;
                for (int u = 0; u < vertexCount; u++) {
                    if (dist[u] != INF) {
                        for (Pair v_w : adjList.get(u)) {
                            int v = v_w.first();
                            double w = v_w.second();
                            relaxCheckModified(u, v, w);
                        }
                    }
                }
                if (!modified) { // If no modifications in a pass, there are no negative cycle
                    break; // So we can terminate bellmanFord here
                }
            }

            // one more pass to check for negative weight cycle
            for (int u = 0; u < vertexCount; u++) {
                if (dist[u] != INF) {
                    for (Pair v_w : adjList.get(u)) {
                        int v = v_w.first();
                        double w = v_w.second();
                        relaxCheckNegativeCycle(u, v, w);
                        if (hasNegativeCycle) {
                            return;
                        }
                    }
                }
            }
        }
    }

    static void relaxCheckModified(int u, int v, double w) {
        if (dist[v] > dist[u] + w) {
            dist[v] = dist[u] + w;
            modified = true;
        }
    }

    static void relaxCheckNegativeCycle(int u, int v, double w) {
        if (dist[v] > dist[u] + w) {
            hasNegativeCycle = true;
        }
    }

}