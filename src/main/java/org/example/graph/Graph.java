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
        return new int[numVertices];
    }

    public int[] DAGShortestPath(int source) {
        return new int[numVertices];
    }
}