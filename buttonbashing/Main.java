import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static final int INF = 1000000000;
    static int[] buttons;
    static int buttonCount;
    static int[] pathLength = new int[3601]; // for vertex 0 -> 3600 

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int testCount = io.getInt();
        for (int i = 0; i < testCount; i++) {
            buttonCount = io.getInt();
            int desiredTime = io.getInt();
            buttons = new int[buttonCount];
            // buttons is an array of added time
            for (int j = 0; j < buttonCount; j++) {
                buttons[j] = io.getInt();
            }
            // Set all pathLengths to INF, except pathLength[0] = 0;
            for (int u = 1; u < 3601; u++) {
                pathLength[u] = INF;
            }
            bfs();
            // Print the pathLength of closest time that is equal or greater to desiredTime, and the difference
            for (int t = desiredTime; t < 3601; t++) {
                if (pathLength[t] != INF) {
                    int difference = t - desiredTime;
                    System.out.println(pathLength[t] + " " + difference);
                    break;
                }
            }
        }
        io.close();
    }
    // bfs algo (sssp version) using 0 as starting vertex, record the pathLength from 0 to each vertex u 
    // O(V + E) V is 3601. 

    static void bfs() {
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(0);
        while (!queue.isEmpty()) {
            Integer curr = queue.remove(); 
            // for each neighbor of curr (neighbor is a vertex that is a button's added time away)
            for (int i = 0; i < buttonCount; i++) {
                int neighbor = setInRange(curr + buttons[i]);
                if (pathLength[neighbor] == INF) {
                    pathLength[neighbor] = pathLength[curr] + 1;
                    queue.add(neighbor);
                }
            }
        } 
    }
    static int setInRange(int x) {
        return Math.min(3600, Math.max(0, x));
    }
}