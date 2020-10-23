package pt.fcul.ppc.nnelas.knapsack.threaded_services;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadedCrossoverService {
    private final MultiThreadManager multiThreadManager;
    private final int populationSize;

    public ThreadedCrossoverService(MultiThreadManager multiThreadManager, int populationSize) {
        this.multiThreadManager = multiThreadManager;
        this.populationSize = populationSize;
    }

    public Individual[] crossover(Individual[] population) {
        Individual[] newPopulation = new Individual[populationSize];
        newPopulation[0] = population[0]; // The best individual remains

        multiThreadManager.execute((int startIndex, int endIndex) -> {
            // The first elements in the population have higher probability of being selected
            IntStream.range(startIndex, endIndex).parallel().forEachOrdered(i -> {
                int dadIndex = getRandomIndex();
                int momIndex = getRandomIndex();
                newPopulation[i] = population[dadIndex].crossoverWith(population[momIndex]);
            });
        }, populationSize);

        return newPopulation;
    }

    private int getRandomIndex() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return (int) (-Math.log(r.nextDouble()) * populationSize) % populationSize;
    }
}
