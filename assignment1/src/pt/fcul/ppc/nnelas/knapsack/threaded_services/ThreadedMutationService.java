package pt.fcul.ppc.nnelas.knapsack.threaded_services;

import pt.fcul.ppc.nnelas.knapsack.Individual;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadedMutationService {
    private final MultiThreadManager multiThreadManager;
    private final int populationSize;
    private final double probMutation;

    public ThreadedMutationService(
            MultiThreadManager multiThreadManager, int populationSize, double probMutation) {
        this.multiThreadManager = multiThreadManager;
        this.populationSize = populationSize;
        this.probMutation = probMutation;
    }

    public void mutate(Individual[] newPopulation) {
        multiThreadManager.execute((int startIndex, int endIndex) -> {
            IntStream.range(startIndex, endIndex).parallel().forEachOrdered(i -> {
                ThreadLocalRandom r = ThreadLocalRandom.current();
                if (r.nextDouble() < probMutation) {
                    newPopulation[i].mutate();
                }
            });
        }, populationSize);
    }
}
