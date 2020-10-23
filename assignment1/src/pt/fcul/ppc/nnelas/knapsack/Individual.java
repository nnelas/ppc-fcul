package pt.fcul.ppc.nnelas.knapsack;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Individual {

    // This is the definition of the problem
    public static final int GENE_SIZE = 1000; // Number of possible items
    public static int[] VALUES = new int[GENE_SIZE];
    public static int[] WEIGHTS = new int[GENE_SIZE];
    public static int WEIGHT_LIMIT = 300;


    static {
        // This code initializes the problem.
        Random r = new Random(1L);
        for (int i = 0; i < GENE_SIZE; i++) {
            VALUES[i] = r.nextInt(100);
            WEIGHTS[i] = r.nextInt(100);
        }
    }

    /*
     *  This array corresponds to whether the object at a given index
     *  is selected to be placed inside the knapsack.
     *  The goal is to find the items that maximize the total value with
     *  surpassing the weight limit.
     */
    public boolean[] selectedItems = new boolean[GENE_SIZE];
    public int fitness;

    public static Individual createRandom() {
        Individual ind = new Individual();

        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < GENE_SIZE; i++) {
            ind.selectedItems[i] = r.nextBoolean();
        }
        return ind;
    }

    /*
     * This method evaluates how good a solution the current individual is.
     * Returns +totalValue if within the weight limit, otherwise returns
     * -overlimit. The goal is to maximize the fitness.
     */
    public void measureFitness() {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < GENE_SIZE; i++) {
            if (selectedItems[i]) {
                totalValue += VALUES[i];
                totalWeight += WEIGHTS[i];
            }
        }
        this.fitness = totalWeight > WEIGHT_LIMIT ? -(totalWeight - WEIGHT_LIMIT) : totalValue;
    }

    /*
     * Generates a random point in the genotype (selected Items)
     * Until that point, uses genes from dad (current)
     * After that point, uses genes from mom (mate)
     */
    public Individual crossoverWith(Individual mate) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int crossoverPoint = r.nextInt(GENE_SIZE);

        Individual child = new Individual();
        for (int i = 0; i < GENE_SIZE; i++) {
            if (i < crossoverPoint) {
                child.selectedItems[i] = this.selectedItems[i];
            } else {
                child.selectedItems[i] = mate.selectedItems[i];
            }
        }
        return child;
    }

    public void mutate() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int mutationPoint = r.nextInt(GENE_SIZE);
        this.selectedItems[mutationPoint] = !this.selectedItems[mutationPoint];
    }

    @Override
    public String toString() {
        // to debug sorting algorithm
        return String.valueOf(fitness);
    }
}
