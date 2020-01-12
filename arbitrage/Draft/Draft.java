import java.util.*;
public class Draft {
    public static final double INF = 1000000000;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
    
        while (true) {
            int currencies = Integer.parseInt(in.nextLine());
            if (currencies == 0) {
                break;
            }
            String[] currencyCodes = in.nextLine().split(" ");
            int num_currency_pairs = Integer.parseInt(in.nextLine());
            String[][] currency_pairs = new String[num_currency_pairs][2];
            double[] weights = new double[num_currency_pairs];
            for (int i = 0; i < num_currency_pairs; i++) {
                String[] pair = in.nextLine().split(" ");
                currency_pairs[i][0] = pair[0];
                currency_pairs[i][1] = pair[1];
                String[] temp = pair[2].split(":");
                int a = Integer.parseInt(temp[0]);
                int b = Integer.parseInt(temp[1]);
                weights[i] = -(Math.log( (double)b / a));
            }
            
            if (BellmanFord(currencyCodes, currency_pairs, weights, num_currency_pairs)) {
                System.out.println("Ok");
            } else {
                System.out.println("Arbitrage");
            }
        }
        in.close();
    }
     // Bellman Ford's routine, basically = relax all E edges V-1 times
    public static boolean BellmanFord(String[] vertices, String[][] edges, double[] weights, int edgeCount) {
        HashMap<String, Double> dist = new HashMap<>();
        for (int k = 0; k < vertices.length; k++) {
        // initialize all dist to INF, except for vertex k which has 0 dist
            for (int i = 0; i < vertices.length; i++) {
                dist.put(vertices[i], INF);
            }
            dist.put(vertices[k], (double)0);

            // relax edges n-1 times
            for (int i = 0; i < vertices.length - 1; i++) {
                for (int j = 0; j < edgeCount; j++) {
                    if (dist.get(edges[j][0]) + weights[j] < dist.get(edges[j][1])) {
                        dist.put(edges[j][1], dist.get(edges[j][0]) + weights[j]);
                    }
                }
            }

            // check for negative-weight cycles
            for (int j = 0; j < edgeCount; j++) {
                if (dist.get(edges[j][0]) + weights[j] < dist.get(edges[j][1])) {
                    return false;
                }
            }

        }
        return true;
    }
}