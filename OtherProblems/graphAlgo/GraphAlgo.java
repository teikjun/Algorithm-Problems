import java.util.*;

class graphAlgo {
	int V;
	boolean directed;
	ArrayList<ArrayList<Integer>> adjList;
	ArrayList<ArrayList<IntegerPair>> AL;
	final int VISITED = 1;
	final int UNVISITED = 0;
	final int EXPLORED = 2;
	final int INF = 1000000000;

	// explores all vertices and marks them as visited
	void bfs(int s) {
		int[] visited = new int[V];
		Queue<Integer> queue = new LinkedList<Integer>();
		visited[s] = VISITED;
		queue.add(s);
		while (!queue.isEmpty()) {
			Integer curr = queue.remove();
			ArrayList<Integer> adjacentVertices = adjList.get(curr);
			for (Integer a : adjacentVertices) {
				if (visited[a] == UNVISITED) {
					visited[a] = VISITED;
					queue.add(a);
				}
			}
		}
	}

	void dfs(int s) {
		int[] visited = new int[V];
		Stack<Integer> stack = new Stack<Integer>();
		visited[s] = VISITED;
		stack.push(s);
		while (!stack.isEmpty()) {
			Integer curr = stack.pop();
			ArrayList<Integer> adjacentVertices = adjList.get(curr);
			for (Integer a : adjacentVertices) {
				if (visited[a] == UNVISITED) {
					visited[a] = VISITED;
					stack.push(a);
				}
			}
		}
	}

	void dfsRec(int s) {
		int[] visited = new int[V];
		visited[s] = VISITED;
		ArrayList<Integer> adjacentVertices = adjList.get(s);
		for (Integer a : adjacentVertices) {
			if (visited[a] == UNVISITED) {
				visited[a] = VISITED;
				dfsRec(a);
			}
		}
	}

	void dijkstra(int s) {
		ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(V, INF)); // INF = 1e9 here
		dist.set(s, 0);
		TreeSet<IntegerPair> pq = new TreeSet<>(); // balanced BST version

		for (int u = 0; u < V; u++) { // dist[u] = INF
			pq.add(new IntegerPair(dist.get(u), u)); // but dist[s] = 0
		}
		// sort the pairs by non-decreasing distance from s
		while (!pq.isEmpty()) { // main loop
			IntegerPair top = pq.pollFirst();
			int u = top.second(); // shortest unvisited u
			for (IntegerPair v_w : AL.get(u)) { // all edges (adjacent vertices, weight) from u
				// relax operation
				int v = v_w.first();
				int w = v_w.second();
				relaxDijkstra(u, v, w, dist, pq);
			}
		}
	}

	void relaxDijkstra(int u, int v, int weight_u_v, ArrayList<Integer> dist, TreeSet<IntegerPair> pq) {
		if (dist.get(v) > dist.get(u) + weight_u_v) {
			pq.remove(new IntegerPair(dist.get(v), v)); // erase old pair
			dist.set(v, dist.get(u) + weight_u_v);
			pq.add(new IntegerPair(dist.get(v), v)); // enqueue better pair
		}
	}

	void modifiedDijkstra(int s) {
		ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(V, INF)); // INF = 1e9 here
		dist.set(s, 0);
		PriorityQueue<IntegerPair> pq = new PriorityQueue<>();
		pq.offer(new IntegerPair(0, s));

		// sort the pairs by non-decreasing distance from s
		while (!pq.isEmpty()) { // main loop
			IntegerPair top = pq.poll();
			int d = top.first();
			int u = top.second(); // shortest unvisited u
			if (d > dist.get(u)) { // a very important check
				continue;
			}
			for (IntegerPair v_w : AL.get(u)) { // all edges from u
				// relax operation
				int v = v_w.first();
				int w = v_w.second();
				relaxModifiedDijkstra(u, v, w, dist, pq);
			}
		}
	}

	void relaxModifiedDijkstra(int u, int v, int weight_u_v, ArrayList<Integer> dist, PriorityQueue<IntegerPair> pq) {
		if (dist.get(v) > dist.get(u) + weight_u_v) {
			dist.set(v, dist.get(u) + weight_u_v); // relax operation
			pq.offer(new IntegerPair(dist.get(v), v)); // enqueue better pair
		}
	}

	int[] dist;
	int[] prev;

	void relax(int u, int v, int weight_u_v) {
		if (dist[v] > dist[u] + weight_u_v) {
			dist[v] = dist[u] + weight_u_v;
			prev[v] = u;
		}
	}


	void bellmanFord(int s) {
		// Bellman Ford's routine, basically = relax all E edges V-1 times
		ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(V, INF));
		dist.set(s, 0); // INF = 1e9 here

		for (int i = 0; i < V - 1; i++) { // total O(V*E)
			Boolean modified = false; // optimization
			for (int u = 0; u < V; u++) { // these two loops = O(E)
				if (dist.get(u) != INF) { // important check
					for (IntegerPair v_w : AL.get(u)) { // all adjacent vertices
						int v = v_w.first(), w = v_w.second();
						// relax
						if (dist.get(v) > dist.get(u) + w) {
							dist.set(v, dist.get(u) + w); // relax operation
							modified = true; // optimization
						}
					}
				}
			}
			if (!modified) {
				break;
			}
		}
		// check for negative cycle
		Boolean hasNegativeCycle = false;
		for (int u = 0; u < V; u++) { // one more pass to check
			if (dist.get(u) != INF) {
				for (IntegerPair v_w : AL.get(u)) {
					int v = v_w.first(), w = v_w.second();
					if (dist.get(v) > dist.get(u) + w) {// should be false
						hasNegativeCycle = true; // if true => -ve cycle
					}
				}
			}
		}
		// print output
		System.out.printf("Negative Cycle Exist? %s\n", hasNegativeCycle ? "Yes" : "No");
		if (!hasNegativeCycle) {
			for (int u = 0; u < V; ++u) {
				System.out.printf("SSSP(%d, %d) = %d\n", s, u, dist.get(u));
			}
		}
	}

}