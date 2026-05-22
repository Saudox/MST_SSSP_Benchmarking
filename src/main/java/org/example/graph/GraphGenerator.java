package org.example.graph;

import java.util.Random;

public class GraphGenerator {
    private static final int MIN_WEIGHT = 1;
    private static final int MAX_WEIGHT = 1000;
    private static final Random rand = new Random(42);

    private static int getRandomWeight() {
        return rand.nextInt(MAX_WEIGHT - MIN_WEIGHT + 1) + MIN_WEIGHT;
    }

    // Sparse Graph with E ~ 5V
    public static Graph generateSparseGraph(int v) {
        Graph g = new Graph(v);
        // 1. Create a spanning tree to guarantee it's connected
        for (int i = 0; i < v - 1; i++) {
            g.addEdge(i, i + 1, getRandomWeight());
        }

        int targetEdges = 5 * v;
        int currentEdges = v - 1;

        while (currentEdges < targetEdges) {
            int u = rand.nextInt(v);
            int dest = rand.nextInt(v);
            if (u != dest) { // prevent self loops
                g.addEdge(u, dest, getRandomWeight());
                currentEdges++;
            }
        }
        return g;
    }

    // Dense Graph with E ~ 25% of max edges
    public static Graph generateDenseGraph(int v) {
        Graph g = new Graph(v);
        // Create a spanning tree so it's connected
        for (int i = 0; i < v - 1; i++) {
            g.addEdge(i, i + 1, getRandomWeight());
        }

        for (int i = 0; i < v; i++) {
            // Starting from i + 2 to avoid duplicating the spanning tree edges
            for (int j = i + 2; j < v; j++) {
                if (rand.nextDouble() < 0.25) {
                    g.addEdge(i, j, getRandomWeight());
                }
            }
        }
        return g;
    }

    // Complete Graph
    public static Graph generateCompleteGraph(int v) {
        Graph g = new Graph(v);
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                g.addEdge(i, j, getRandomWeight());
            }
        }
        return g;
    }

    // DAG
    public static Graph generateDAG(int v) {
        Graph g = new Graph(v);
        for (int i = 0; i < v - 1; i++) {
            g.addDirectedEdge(i, i + 1, getRandomWeight());
        }

        int targetEdges = 5 * v;
        int currentEdges = v - 1;

        while (currentEdges < targetEdges) {
            int u = rand.nextInt(v - 1);
            // v must be strictly greater than u to ensure that there are no cycles
            int dest = u + 1 + rand.nextInt(v - u - 1);

            g.addDirectedEdge(u, dest, getRandomWeight());
            currentEdges++;
        }
        return g;
    }
}