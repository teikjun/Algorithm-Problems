import java.util.*;

public class UnweightedGraph {
	
	int V;
	boolean directed;
	ArrayList<ArrayList<Integer>> adjList;
	int[] visited;
	
	final int VISITED = 1;
	final int UNVISITED = 0;
	final int EXPLORED = 2; // to be used for detecting directed cycles
	
	UnweightedGraph(int nodes, boolean directed){
		if (nodes <= 0){
			throw new IllegalArgumentException("Number of nodes must be positive!");
		}
		V = nodes;
		this.directed = directed;
		adjList = new ArrayList<>();
		for (int i = 0; i < V; i++){
			adjList.add(new ArrayList<Integer>());
		}
	}
	
	void addEdge(int u, int v){
		if (directed){
			adjList.get(u).add(v);
		} else {			
			adjList.get(u).add(v);
			adjList.get(v).add(u);
		}
	}
	
	/**
	 * Performs a DFS on the entire graph. That is, this method iterates through the vertices 0 to V-1. 
	 * If a vertex u has not been traversed yet, then a DFS will be performed with u as the source, by calling
	 * dfsRecurse(u)
	 */
	void dfs(){
		visited = new int[V];
		for (int u = 0; u < V; u++) {
			if (visited[u] == UNVISITED) {
				dfsRecurse(u);
			}
		}
	}
	
	/**
	 * Helper method for dfs. When performing dfsRecurse(u), you should mark u as visited. 
	 * Then you should take all of the neighbours of u and recursively visit the 
	 * neighbours which are unvisited. 
	 */
	void dfsRecurse(int u){
		visited[u] = VISITED;
		ArrayList<Integer> adjacentVertices = adjList.get(u);
		for (Integer a : adjacentVertices) {
			if (visited[a] == UNVISITED) {
				dfsRecurse(a);		
			}
		}
	}
	
	/**
	 * Counts the number of connected components in the graph
	 * @return number of connected components
	 * Hint: Use dfsRecurse as a subrountine.
	 */
	int getNumConnectedComponents(){
		visited = new int[V];
		int count = 0;
		for (int u = 0; u < V; u++) {
			if (visited[u] == UNVISITED) {
				count++; 
				dfsRecurse(u);
			}
		}
		return count;
	}
	
	/**
	 * Given a Directed Acyclic Graph(DAG), this function will return a valid topological ordering of the graph. 
	 * You may use the `hasCycle` function as a validator to ensure you do not use this function on a cyclic or 
	 * undirected graph
	 */
	
	List<Integer> topoOrder; //class variable to store your topoOrder
	List<Integer> getToposortOrder() {
		topoOrder = new ArrayList<Integer>(); 
		visited = new int[V];
		for (int u = 0; u < V; u++) {
			if (visited[u] == UNVISITED) {
				dfsTopoRecurse(u);
			}	
		}
		Collections.reverse(topoOrder);
		return topoOrder; 
	}

	/**
	 * Helper method when doing toposort.Very similar to dfsRecurse. You may not require this if you
	 * are implementing a topological sort with Kahn's
	 */
	void dfsTopoRecurse(int u){
		ArrayList<Integer> adjacentVertices = adjList.get(u);
		for (Integer a : adjacentVertices) {
			if (visited[a] == UNVISITED) {
				dfsTopoRecurse(a);		
			}
		}
		visited[u] = VISITED;
		topoOrder.add(u);
	}
	
	/**
	 * This will return true if there is a directed cycle within your graph. Note that you may also
	 * use this to determine whether there is an undirected cycle as well (all the tests are on directed cycle only)
	 */
	int[] explored; //serves similar purpose as visited array. Similar to Q3(a) of the Discussion Group
	boolean hasCycle;
	boolean hasCycle(){
		hasCycle = false;
		visited = new int[V];
		for (int u = 0; u < V; u++) {
			if (visited[u] == UNVISITED) {
				dfsCycle(u);
			}
		}
		return hasCycle;
	}
	
	/**
	 *  Helper method for DFS cycle detection (for the directed case)
	 */
	void dfsCycle(int u){
		visited[u] = EXPLORED;
		ArrayList<Integer> adjacentVertices = adjList.get(u);
		for (Integer a : adjacentVertices) {
			if (visited[a] == UNVISITED) {
				dfsCycle(a);		
			} else if (visited[a] == EXPLORED) {
				hasCycle = true;
			}
		}
		visited[u] = VISITED;
	}
	
	/**
	 * Performs a DFS on the entire graph. That is, this method iterates through the vertices 0 to V-1.
	 * You should use a Queue to implement this
	 */
	void bfs() {
		visited = new int[V];
		Queue<Integer> queue = new LinkedList<Integer>();
		for (int u = 0; u < V; u++) {
			if (visited[u] == UNVISITED) {
				visited[u] = VISITED;
				queue.add(u);
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
		}
	}
	
	/**
	 * Returns true if the given graph is bicolorable (i.e. bipartite). Similar to Q1(a) of the Discussion group
	 */
	
	boolean isBipartiteGraph(){
		int[] colors = new int[V]; // 0 - unvisited, 1 - visited with color 1, 2 - visited with color 2
		Queue<Integer> queue = new LinkedList<Integer>();
		for (int u = 0; u < V; u++) {
			if (colors[u] == 0) {
				queue.add(u);
				colors[u] = 1;
				while (!queue.isEmpty()) {
					Integer curr = queue.remove();
					ArrayList<Integer> adjacentVertices = adjList.get(curr);
					for (Integer a : adjacentVertices) {
						if (colors[a] == 0) {
							queue.add(a);
							colors[a] = (colors[curr] == 1) ? 2 : 1;
						} else if (colors[a] == colors[curr]) {
							return false; 
						}
					}
				}
			}
		}
		return true;
	}
}
