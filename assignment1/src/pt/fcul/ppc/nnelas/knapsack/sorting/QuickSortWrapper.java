package pt.fcul.ppc.nnelas.knapsack.sorting;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.ForkJoinPool;

public class QuickSortWrapper implements SortingAlgorithm {
    private final int numberOfThreads;

    public QuickSortWrapper(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void sort(Individual[] population, int startIndex, int endIndex) {
        ForkJoinQuickSort quickSort = new ForkJoinQuickSort(population, startIndex, endIndex);
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        pool.invoke(quickSort);
        quickSort.join();
    }
}
