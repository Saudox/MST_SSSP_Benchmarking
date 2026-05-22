package org.example;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    public void testMSTAlgorithms() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 20);
        g.addEdge(1, 3, 50);
        g.addEdge(2, 3, 20);
        g.addEdge(3, 4, 10);

        List<Edge> prim = g.primMST();
        List<Edge> kruskal = g.kruskalMST();

        int primWeight = prim.stream().mapToInt(e -> e.weight).sum();
        int kruskalWeight = kruskal.stream().mapToInt(e -> e.weight).sum();

        assertEquals(60, primWeight, "Prim's MST weight should be 60");
        assertEquals(60, kruskalWeight, "Kruskal's MST weight should be 60");
    }

    @Test
    public void testShortestPathAlgorithms() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 5);
        g.addDirectedEdge(0, 2, 3);
        g.addDirectedEdge(1, 3, 2);
        g.addDirectedEdge(2, 1, 1);

        int[] dijkstra = g.dijkstra(0);
        int[] dag = g.DAGShortestPath(0);

        assertEquals(4, dijkstra[1], "Shortest path to vertex 1 should be 4");
        assertEquals(4, dag[1], "DAG shortest path to vertex 1 should be 4");
        assertEquals(6, dijkstra[3], "Shortest path to vertex 3 should be 6");
    }
}