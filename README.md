# Assignments for Parallel and Concurrent Programming class @ FCUL
## assignment1: A Genetic Algorithm for The Knapsack Problem
### The Knapsack Problem
Given a set of n objects, each having a value v and a weight w, we want to 
find the set of k objects that maximize the total value but has a total weight 
under MAXWEIGHT_.

Given the complexity of the problem, we will use a heuristic method to find 
a good enough solution.

### Genetic Algorithm
The genetic algorithm for the Knapsack uses an array of booleans as the 
genotype. The algorithm's pseudocode is as follows:
```
population = createRandomPopulation()

while (gen < NGENERATIONS):
  evaluateFitness(population)
  population = sortPopulationByDecreasingFitness(population)
  population = performCrossOver(population)
  population.mutate()

best = population[0]
```
The population is an array of individuals. Each individual is represented by 
its genotype and has a fitness (a boolean representing -extraWeight if the 
totalWeight is higher than the acceptable limit, or +totalValue otherwise ).

Each generation, the fitness is calculated for each individual. The order in 
the population array will be used when selecting parents to mate in the 
CrossOver. Given two random parents (with higher fitness individuals being 
more probable), the child will have the first [0..x] elements from parent 1 
and [x..n] elements from the other parent. The crossover point x is randomly 
generated between 0 and n (the number of genes). The best element of a 
generation is always kept alive into the next generation, to have a 
monotonically increasing fitness function in regards to the generation.

There is also a chance that individuals can be mutated. Mutations bit-flip 
one gene at a given random point in their gene.

After NGEN generations, the process stops, and we have the best so far.

### The Task
Parallelize the provided code in the most advantageous places. You can 
rewrite the code as much as you want. 

You can use Threads, ForkJoin or Streams, but...

Important: You have to hand-write a parallel sorting algorithm (MergeSort, 
QuickSort or other).

Write a short report on the work done (details below).

## Submission Instructions
You should submit your assignment via Moodle as a zip file with your code 
and a PDF or txt report.

The report is as important as the working code — This is a Masters-level 
course after all! — and will be evaluated as such. Your report should include 
the following:
- description of how parallelization was applied
- the rationale for the parallelization method used
- measurements showing whether parallelization was advantageous in each case

The report should not exceed one A4 page.

