package pt.fcul.ppc.nnelas.knapsack.sorting;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<Void> {
    private static final long serialVersionUID = 1L;
    private final Individual[] population;
    private final int leftIndex;
    private final int rightIndex;

    public ForkJoinMergeSort(Individual[] population, int leftIndex, int rightIndex) {
        this.population = population;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    @Override
    protected Void compute() {
        if (leftIndex < rightIndex) {
            int middlePoint = (leftIndex + rightIndex) / 2;

            ForkJoinMergeSort forkA = new ForkJoinMergeSort(population, leftIndex, middlePoint);
            forkA.fork();

            ForkJoinMergeSort forkB = new ForkJoinMergeSort(
                    population, middlePoint + 1, rightIndex);
            forkB.fork();

            forkA.join();
            forkB.join();
            merge(population, leftIndex, middlePoint, rightIndex);
        }

        return null;
    }

    void merge(Individual[] population, int leftIndex, int middlePoint, int rightIndex) {
        int n1 = middlePoint - leftIndex + 1;
        int n2 = rightIndex - middlePoint;

        Individual[] leftArray = new Individual[n1];
        Individual[] rightArray = new Individual[n2];

        if (n1 >= 0) System.arraycopy(population, leftIndex, leftArray, 0, n1);
        if (n2 >= 0) System.arraycopy(population, middlePoint + 1, rightArray, 0, n2);

        int i = 0, j = 0;
        int k = leftIndex;
        while (i < n1 && j < n2) {
            if (rightArray[j].fitness <= leftArray[i].fitness) {
                population[k] = leftArray[i];
                i++;
            } else {
                population[k] = rightArray[j];
                j++;
            }
            k++;
        }

        copyLeftovers(population, n1, leftArray, i, k);
        copyLeftovers(population, n2, rightArray, j, k);
    }

    private void copyLeftovers(Individual[] population, int n1, Individual[] splitPopulation,
                               int i, int k) {
        while (i < n1) {
            population[k] = splitPopulation[i];
            i++;
            k++;
        }
    }
}
