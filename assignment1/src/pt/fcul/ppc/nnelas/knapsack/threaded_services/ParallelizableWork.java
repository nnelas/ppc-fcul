package pt.fcul.ppc.nnelas.knapsack.threaded_services;

@FunctionalInterface
public interface ParallelizableWork {
    void doIteration(int start, int end);
}
