package org.example;

import org.example.graph.BenchmarkRunner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Graph Algorithms Benchmark...\n");
        BenchmarkRunner.runAllBenchmarks();
        System.out.println("\nBenchmarking complete.");
    }
}