package pt.fcul.ppc.nnelas.knapsack.sorting;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.ForkJoinPool;

public class MergeSortWrapper implements SortingAlgorithm {
    private final int numberOfThreads;

    public MergeSortWrapper(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void sort(Individual[] population, int startIndex, int endIndex) {
        ForkJoinMergeSort mergeSort = new ForkJoinMergeSort(population, startIndex, endIndex);
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        pool.invoke(mergeSort);
        mergeSort.join();
    }
}
