import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
/* 
Vertices: Lectures, keep information about (campus, start, end) in an array
Edges: route connecting lectures (can be taken iff it is possible to reach the other lecture on time)
Weight: shortest time needed to reach the other lecture.

1. Run APSP to get the shortest path between all vertices (use this as edge weight)  O(V ^ 3)
2. Sort the vertices in ascending order of start time  O(V log V)
3. Run modified BFS starting from each vertex and find the no. of hops required to reach the furthest vertex
    O( V (V + Elog(V)) ) 

Modification for BFS: for each vertex, keep an array entry for the number of vertex visited
up to and including that vertex (completeCount). 
The vertex can be visited again iff the completeCount is greater than the previous recorded value 
*/ 
public class MaxLectures {
    static int maxLectureCount;
    static int campusCount;
    static int vertexCount;
    static IntTriple[] lectureInfo;
    static int[][] shortestPath;
    static int[] visited;
    static ArrayList<ArrayList<Integer>> adjacencyList;

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int testCount = io.getInt();
        for (int i = 0; i < testCount; i++) {
            campusCount = io.getInt();
            vertexCount = io.getInt(); // number of lectures
            int edgeCount = campusCount * (campusCount - 1) / 2; // number of routes

            shortestPath = new int[campusCount][campusCount];
            lectureInfo = new IntTriple[vertexCount];
            adjacencyList= new ArrayList<ArrayList<Integer>>();
            maxLectureCount = 0; 
            // Initially, table records the weight of the edge connecting u and v
            // if u == v, the table records 0. 
            for (int j = 0; j < edgeCount; j++) {
                int u = io.getInt();
                int v = io.getInt();
                int w = io.getInt();
                shortestPath[u][v] = w;
                shortestPath[v][u] = w;
            }
            for (int k = 0; k < vertexCount; k++) {
                int campus = io.getInt();
                int start = io.getInt();
                int end = io.getInt();
                lectureInfo[k] = new IntTriple(campus, start, end);
                adjacencyList.add(new ArrayList<Integer>());
            }
            // After floydWarshall, shortestPath table contains the shortest paths from any vertex u to v
            // This is the edge weight
            floydWarshall();
            Arrays.sort(lectureInfo);
            // populate the adjacencyList with the neighbors of each vertex
            for (int u = 0; u < vertexCount; u++) {
                addNeighbors(u);
            }
            // run DP algorithm on each vertex that has not been visited
            dpAlgo();
            System.out.println(maxLectureCount);
        }
        io.close();
    }

    // Get all pair shortest path between campuses using floydWarshall 
    public static void floydWarshall() {
        // For sets P0, P1, P2, ..., for every pair (u, v)
        for (int k = 0; k < campusCount; k++) {
            for (int u = 0; u < campusCount; u++) {
                for (int v = 0; v < campusCount; v++) {
                    shortestPath[u][v] = Math.min(shortestPath[u][v], shortestPath[u][k] + shortestPath[k][v]);
                }
            }
        }
    }
    
    /*  for vertices u (0...vertexCount-1), for each unvisited vertex, relax its neighbors
    relax: we store the number of vertices visited so far (including this vertex) as completeCount
    A neighboring vertex v is only relaxed if completeCount[curr] + 1 > completeCount[n] */
    
    public static void dpAlgo() {
        int maxCompleteCount = 1;
        int[] completeCount = new int[vertexCount];
        // initialize all entries of completeCount to 1
        for (int i = 0; i < vertexCount; i++) {
            completeCount[i] = 1;
        }
        // for vertices (0...vertexCount-1), for each unvisited vertex, relax its neighbors
        for (int u = 0; u < vertexCount; u++) {
            ArrayList<Integer> neighborList = adjacencyList.get(u);
            for (Integer v : neighborList) {
                // relax operation
                if (completeCount[u] + 1 > completeCount[v]) {
                    completeCount[v] = completeCount[u] + 1;
                    maxCompleteCount = Math.max(maxCompleteCount, completeCount[v]);
                }
            }    
        }
        maxLectureCount = Math.max(maxLectureCount, maxCompleteCount);
    }

    /*
    A vertex v is a neighbor if 
    (endTime of current vertex) + shortestPath[currCampus][otherCampus] <= (startTime of v)
    */
    public static void addNeighbors(int u) {
        ArrayList<Integer> neighborList = adjacencyList.get(u);
        
        IntTriple currInfo = lectureInfo[u];
        int currCampus = currInfo.first();
        int currEnd = currInfo.third();

        for (int v = 0; v < vertexCount; v++) {
            int otherCampus = lectureInfo[v].first();
            int otherStart = lectureInfo[v].second();

            if (currEnd + shortestPath[currCampus][otherCampus] <= otherStart) {
                neighborList.add(v);
            }
        }
    }


    // search the lectureInfo array for the first lecture that has start time 
    // if none can be found, return a large number 1e9 == 10 ^ 9
    public static int binarySearchStart(int time) {
        int low = 0;
        int high = vertexCount - 1;
        int result = vertexCount; // not found 
        int mid = low;
        while (low <= high) {
            mid = (low + high) / 2;
            if (lectureInfo[mid].second() >= time) {
                result = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    public static ArrayList<Integer> getNeighbors(int curr) {
        ArrayList<Integer> neighborList = new ArrayList<>();

        IntTriple currInfo = lectureInfo[curr];
        int currCampus = currInfo.first();
        int currEnd = currInfo.third();
        int firstNeighbor = binarySearchStart(currEnd);

        for (int i = firstNeighbor; i < vertexCount; i++) {
            int otherCampus = lectureInfo[i].first();
            int otherStart = lectureInfo[i].second();
            //System.out.println(currEnd + " " + shortestPath[currCampus][otherCampus] + " " + otherStart);
            if (currEnd + shortestPath[currCampus][otherCampus] <= otherStart) {
                neighborList.add(i);
            }
        }
        return neighborList;
    }
}