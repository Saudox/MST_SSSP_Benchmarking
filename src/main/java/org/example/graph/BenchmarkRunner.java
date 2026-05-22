package org.example.graph;

import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {
    private static final int WARMUP_RUNS = 5;
    private static final int MEASUREMENT_RUNS = 10;
    private static final int V = 5000;

    public static void runAllBenchmarks() {
        System.out.println("Generating Graphs (V = " + V + ")");
        Graph sparse = GraphGenerator.generateSparseGraph(V);
        Graph dense = GraphGenerator.generateDenseGraph(V);
        Graph complete = GraphGenerator.generateCompleteGraph(V);
        Graph dag = GraphGenerator.generateDAG(V);
        System.out.println("Graphs generated.\n");

        Random rand = new Random(42);

        int source = rand.nextInt(V);

        System.out.println("================ 1. MST BENCHMARKS ================");
        benchmarkMST("Sparse Graph", sparse);
        benchmarkMST("Dense Graph", dense);
        benchmarkMST("Complete Graph", complete);

        System.out.println("\n================ 2. SSSP BENCHMARKS (Dijkstra) ================");
        benchmarkSSSP("Sparse Graph", sparse,source) ;
        benchmarkSSSP("Dense Graph", dense, source);
        benchmarkSSSP("Complete Graph", complete, source);
        benchmarkSSSP("DAG Topology", dag, source);

        System.out.println("\n================ 3. DAG SPECIFIC BENCHMARK ================");
        benchmarkDAG(dag, source);
    }

    private static void benchmarkMST(String graphName, Graph g) {
        System.out.println("--- " + graphName + " ---");
        long[] primTimes = new long[MEASUREMENT_RUNS];
        long[] kruskalTimes = new long[MEASUREMENT_RUNS];

        // Warmup and Measure Prim's
        for (int i = 0; i < WARMUP_RUNS; i++) {
            g.primMST();
        }
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            long start = System.nanoTime();
            g.primMST();
            long end = System.nanoTime();
            primTimes[i] = end - start;
        }
        calculateAndPrintStats("Prim's", primTimes);

        // Warmup and Measure Kruskal's
        for (int i = 0; i < WARMUP_RUNS; i++) {
            g.kruskalMST();
        }
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            long start = System.nanoTime();
            g.kruskalMST();
            long end = System.nanoTime();
            kruskalTimes[i] = end - start;
        }
        calculateAndPrintStats("Kruskal's", kruskalTimes);
    }

    private static void benchmarkSSSP(String graphName, Graph g, int source) {
        System.out.println("--- " + graphName + " (Source: " + source + ") ---");
        long[] dijkstraTimes = new long[MEASUREMENT_RUNS];

        // Warmup and Measure Dijkstra
        for (int i = 0; i < WARMUP_RUNS; i++) {
            g.dijkstra(source);
        }
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(source);
            long end = System.nanoTime();
            dijkstraTimes[i] = end - start;
        }
        calculateAndPrintStats("Dijkstra", dijkstraTimes);
    }

    private static void benchmarkDAG(Graph g, int source) {
        System.out.println("--- DAG Topology (Source: " + source + ") ---");
        long[] dijkstraTimes = new long[MEASUREMENT_RUNS];
        long[] dagTimes = new long[MEASUREMENT_RUNS];

        // Warmup and Measure Dijkstra
        for (int i = 0; i < 5000; i++) {
            g.dijkstra(source);
        }
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            long start = System.nanoTime();
            g.dijkstra(source);
            long end = System.nanoTime();
            dijkstraTimes[i] = end - start;
        }
        double meanDijkstra = calculateAndPrintStats("Dijkstra", dijkstraTimes);


        // Warmup and Measure DAG Algo
        for (int i = 0; i < 5000; i++) {
            g.DAGShortestPath(source);
        }
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            long start = System.nanoTime();
            g.DAGShortestPath(source);
            long end = System.nanoTime();
            dagTimes[i] = end - start;
        }
        double meanDag = calculateAndPrintStats("DAG Algo", dagTimes);

        double speedup = meanDijkstra / meanDag;
        System.out.printf(">> Speed-up: The DAG linear-time algorithm is %.2fx faster than Dijkstra.\n", speedup);
    }

    private static double calculateAndPrintStats(String label, long[] timesNanos) {
        long[] sorted = timesNanos.clone();
        Arrays.sort(sorted);
        double median = sorted[sorted.length / 2] / 1000000.0;

        double sum = 0;
        for (long t : timesNanos) {
            sum += (t / 1000000.0);
        }
        double mean = sum / timesNanos.length;

        double varianceSum = 0;
        for (long t : timesNanos) {
            double ms = t / 1000000.0;
            varianceSum += Math.pow(ms - mean, 2);
        }
        double stdDev = Math.sqrt(varianceSum / timesNanos.length);

        System.out.printf("%-10s -> Mean: %7.2f ms | Median: %7.2f ms | StdDev: %7.2f ms\n",
                label, mean, median, stdDev);
        return mean;
    }
}