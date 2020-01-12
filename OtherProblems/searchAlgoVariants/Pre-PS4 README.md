## Pre-PS4 README

 The main purpose of this exercise is to help you code out basic graph algorithms so as to help you better for PS4 (as well any
 other time you will need to code out Graph Algorithms).

All the functions to be implemented are contained within the `UnweightedGraph.java` file. 

The `UnweightedGraph.java` is an API that represents a graph object which it constructs using the given constructor as well as the `addEdge` function.Note that the constructor specifies whether the graph you are operating on is directed on undirected. Also note that we make use of an **adjacency list** to store the graph. You may assume that the adjacency list is properly constructed when implementing the other functions and no weird errors will be thrown. Your task here is to implement the other functions that allow you to conduct separate functions that determine the graph properties. These include::
- `dfs()` and `dfsRecurse(int v)` that perform dfs on the graph. The main purpose of the `dfsRecurse(int v)` is to specify which node you are visiting at that moment so that you main input the neighbours. This `dfs()` will start from node 0.  
- `getNumConnectedComponents()` which returns the number of connected components in your `UnweightedGraph`. This is a direct implementation of what was required for 1(b) in the Discussion Group
- `getToposortOrder()` which, given a directed graph, returns a valid topological ordering of the graph. You are free to either use DFS (and hence make use of the helper `dfsTopoRecurse()`) or use Kahn's algorithm to return the required list.
- `hasCycle()` which allows you to detect the existence of cycles in a **directed** graph. If you did not manage to go through 3(a) during your DG, do ask your tutor for help regarding this. Hint, you are supposed to make use of the `EXPLORED` state defined in the graph.
- `bfs()` which is to implement a Breadth First Search on your `UnweightedGraph`, starting from node 0.
- `isBipartiteGraph()` which returns true if the `UnweightedGraph` is bipartite.

**NOTE** `bfs()` and `dfs()` are not explicitly tested with the checker. The purpose of these functions are mainly for debugging when you use the same code as subroutines in higher level functions. 

Once functions are implemented, you may use the checker to run some of the tests. The tests are mainly of the higher level functions such as your topological sort, checking of directed cycles and counting the number of connected components. The running of the checker is similar to how it was done for the PS3 checker. 

If you have any questions with regards to this exercise, feel free to post it on Piazza (or ask your TAs about them). Have fun!