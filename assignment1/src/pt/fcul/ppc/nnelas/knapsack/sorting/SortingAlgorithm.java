package pt.fcul.ppc.nnelas.knapsack.sorting;

import pt.fcul.ppc.nnelas.knapsack.Individual;

public interface SortingAlgorithm {
    void sort(Individual[] population, int startIndex, int endIndex);
}
