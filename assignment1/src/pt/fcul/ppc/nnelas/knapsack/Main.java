package pt.fcul.ppc.nnelas.knapsack;

import pt.fcul.ppc.nnelas.knapsack.sorting.MergeSortWrapper;
import pt.fcul.ppc.nnelas.knapsack.sorting.QuickSortWrapper;
import pt.fcul.ppc.nnelas.knapsack.sorting.SortingAlgorithm;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.MultiThreadManager;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedCrossoverService;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedFitnessCalculatorService;
import pt.fcul.ppc.nnelas.knapsack.threaded_services.ThreadedMutationService;

public class Main {
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int POP_SIZE = 100000;
    private static final double PROB_MUTATION = 0.5;
    private static final int N_GENERATIONS = 500;

    public static void main(String[] args) {
        MultiThreadManager multiThreadManager = new MultiThreadManager(NUMBER_OF_THREADS);

        ThreadedFitnessCalculatorService fitnessCalculatorService =
                new ThreadedFitnessCalculatorService(multiThreadManager, POP_SIZE);
        ThreadedCrossoverService crossoverService = new ThreadedCrossoverService(
                multiThreadManager, POP_SIZE);
        ThreadedMutationService mutationService = new ThreadedMutationService(
                multiThreadManager, POP_SIZE, PROB_MUTATION);

        SortingAlgorithm sortingAlgorithm = new QuickSortWrapper(NUMBER_OF_THREADS);
        // SortingAlgorithm sortingAlgorithm = new MergeSortWrapper(NUMBER_OF_THREADS);

        KnapsackGA ga = new KnapsackGA(
                fitnessCalculatorService, crossoverService, mutationService, sortingAlgorithm,
                N_GENERATIONS, POP_SIZE);

        ga.run();
    }
}
