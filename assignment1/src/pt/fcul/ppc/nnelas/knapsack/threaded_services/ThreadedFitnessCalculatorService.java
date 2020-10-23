package pt.fcul.ppc.nnelas.knapsack.threaded_services;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.stream.IntStream;

public class ThreadedFitnessCalculatorService {
    private final MultiThreadManager multiThreadManager;
    private final int populationSize;

    public ThreadedFitnessCalculatorService(
            MultiThreadManager multiThreadManager, int populationSize) {
        this.multiThreadManager = multiThreadManager;
        this.populationSize = populationSize;
    }

    public void calculateFitness(Individual[] population) {
        multiThreadManager.execute((int startIndex, int endIndex) -> {
            IntStream.range(startIndex, endIndex).parallel().forEachOrdered(
                    i -> population[i].measureFitness());
        }, populationSize);
    }
}
