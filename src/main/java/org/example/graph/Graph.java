package org.example.graph;
import java.util.*;

public class Graph {
    private int numVertices;
    private Map<Integer, List<Edge>> adjList;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

    public List<Edge> getEdges(int u) {
        return adjList.get(u);
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(u, v, weight));
        adjList.get(v).add(new Edge(v, u, weight));
    }

    public void addDirectedEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(u, v, weight));
    }

    public List<Edge> primMST() {
        List<Edge> mst = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        visited[0] = true;
        pq.addAll(adjList.get(0));

        while (!pq.isEmpty()) {
            Edge currentEdge = pq.poll();
            int v = currentEdge.v;

            if (visited[v]) continue; // skip the vertex if it's already in the mst
            visited[v] = true;
            mst.add(currentEdge);

            for (Edge nextEdge : adjList.get(v)) {
                if (!visited[nextEdge.v]) {
                    pq.add(nextEdge);
                }
            }
        }
        return mst;
    }

    public List<Edge> kruskalMST() {
        List<Edge> allEdges = new ArrayList<>();
        for (int u = 0; u < numVertices; u++) {
            for (Edge e : adjList.get(u)) {
                if (u < e.v) {
                    allEdges.add(e);
                }
            }
        }

        Collections.sort(allEdges);

        DSU dsu = new DSU(numVertices);
        List<Edge> mst = new ArrayList<>();

        for (Edge e : allEdges) {
            //union and check if they were in different sets in the first place
            if (dsu.union(e.u, e.v)) {
                mst.add(e);
            }
        }
        return mst;
    }

    public int[] dijkstra(int source) {
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Priority queue storing arrays: [vertex, distance_from_source]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];

            // ignore vertices we already have found the min path to them
            if (d > dist[u]) continue;

            for (Edge edge : adjList.get(u)) {
                int v = edge.v;
                int weight = edge.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    public int[] DAGShortestPath(int source) {
        int[] inDegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (Edge e : adjList.get(u)) {
                inDegree[e.v]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topoOrder.add(u);

            for (Edge e : adjList.get(u)) {
                inDegree[e.v]--;
                if (inDegree[e.v] == 0) {
                    q.add(e.v);
                }
            }
        }

        // Cycle Detection
        if (topoOrder.size() != numVertices) {
            throw new IllegalArgumentException("Graph contains a cycle, Topological sort is not possible here.");
        }

        // Edge relaxation
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        for (int u : topoOrder) {
            // if the vertex is reachable from the source
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge e : adjList.get(u)) {
                    if (dist[u] + e.weight < dist[e.v]) {
                        dist[e.v] = dist[u] + e.weight;
                    }
                }
            }
        }
        return dist;
    }
}