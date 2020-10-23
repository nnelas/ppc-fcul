package pt.fcul.ppc.nnelas.knapsack;

import pt.fcul.ppc.nnelas.knapsack.sorting.SortingAlgorithm;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedCrossoverService;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedFitnessCalculatorService;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedMutationService;

import java.util.stream.IntStream;

public class KnapsackGA {

    private final ThreadedFitnessCalculatorService fitnessCalculatorService;
    private final ThreadedCrossoverService crossoverService;
    private final ThreadedMutationService mutationService;
    private final SortingAlgorithm sortingAlgorithm;
    private final int numberOfGenerations;
    private final int populationSize;
    private Individual[] population;

    public KnapsackGA(
            ThreadedFitnessCalculatorService fitnessCalculatorService,
            ThreadedCrossoverService crossoverService,
            ThreadedMutationService mutationService,
            SortingAlgorithm sortingAlgorithm,
            int numberOfGenerations, int populationSize) {
        this.fitnessCalculatorService = fitnessCalculatorService;
        this.crossoverService = crossoverService;
        this.mutationService = mutationService;
        this.sortingAlgorithm = sortingAlgorithm;
        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.population = new Individual[populationSize];
    }

    public void run() {
        populateInitialPopulationRandomly();

        for (int generation = 0; generation < numberOfGenerations; generation++) {
            calculateFitness();
            sortByDescendingFitness();

            // Debug
            System.out.println("Best fitness at " + generation + " is " + population[0].fitness);

            Individual[] newPopulation = crossover();
            mutate(newPopulation);
            population = newPopulation;
        }
    }

    private void populateInitialPopulationRandomly() {
        IntStream.range(0, populationSize).parallel().forEachOrdered(
                i -> population[i] = Individual.createRandom());
    }

    private void calculateFitness() {
        fitnessCalculatorService.calculateFitness(population);
    }

    private void sortByDescendingFitness() {
        sortingAlgorithm.sort(population, 0, populationSize - 1);
    }

    private Individual[] crossover() {
        return crossoverService.crossover(population);
    }

    private void mutate(Individual[] newPopulation) {
        mutationService.mutate(newPopulation);
    }
}
