public class UnionFindDisjointSet {   
    int[] parent; 
    int[] rank;
    /**
     * Constructs a new Union-Find Disjoint Set with the specified number of vertices, 
     * indexed from 0 to (numVertices - 1).
     * 
     * @param numVertices The number of vertices in this Union-Find Disjoint Set
     */
    public UnionFindDisjointSet(int numVertices) {
        parent = new int[numVertices];
        rank = new int[numVertices];
        // initially parents of each vertex is itself
        for (int i = 0; i < numVertices; i++) {
            parent[i] = i;
        }
    }

    /**
     * Unions the set containing vertex u with the set containing vertex v. If both 
     * vertices u and v are in the same set to begin with, the sets should remain 
     * unchanged.
     */
    public void union(int u, int v) {
        int x = findRoot(u);
        int y = findRoot(v);
        if (x != y) {
            if (rank[x] > rank[y]) {
                parent[y] = x;
            } else {
                parent[x] = y;
            }
            if (rank[x] == rank[y]) {
                rank[y]++;
            }
        }
    }

    /**
     * Checks if vertices u and v belong to the same set.
     * 
     * @return True if vertices u and v belong to the same set, false otherwise. 
     */
    public boolean isSameSet(int u, int v) {
        return findRoot(u) == findRoot(v);
    }

    public int findRoot(int u) {
        int root = u;
        while (parent[root] != root) {
            //System.out.println("compressing");
            //int temp = parent[root];
            parent[root] = parent[parent[root]];
            root = parent[root];
        }
        return root;
    }
}
