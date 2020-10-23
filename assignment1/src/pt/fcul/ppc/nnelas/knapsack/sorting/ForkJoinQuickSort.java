package pt.fcul.ppc.nnelas.knapsack.sorting;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.RecursiveTask;

public class ForkJoinQuickSort extends RecursiveTask<Void> {
    private static final long serialVersionUID = 1L;
    private final Individual[] population;
    private final int startIndex;
    private final int endIndex;

    public ForkJoinQuickSort(Individual[] population, int startIndex, int endIndex) {
        this.population = population;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Void compute() {
        if (startIndex < endIndex) {
            int partitionIndex = partition(population, startIndex, endIndex);

            ForkJoinQuickSort forkA = new ForkJoinQuickSort(population, startIndex,
                    partitionIndex - 1);
            forkA.fork();

            ForkJoinQuickSort forkB = new ForkJoinQuickSort(population,
                    partitionIndex + 1, endIndex);
            forkB.fork();

            forkA.join();
            forkB.join();
        }
        return null;
    }

    private int partition(Individual[] population, int startIndex, int endIndex) {
        Individual pivot = population[endIndex];
        int i = (startIndex - 1);
        for (int j = startIndex; j < endIndex; j++) {
            if (pivot.fitness < population[j].fitness) {
                i++;

                Individual temp = population[i];
                population[i] = population[j];
                population[j] = temp;
            }
        }

        Individual temp = population[i + 1];
        population[i + 1] = population[endIndex];
        population[endIndex] = temp;

        return i + 1;
    }
} 