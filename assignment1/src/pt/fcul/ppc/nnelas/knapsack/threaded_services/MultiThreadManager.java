package pt.fcul.ppc.nnelas.knapsack.threaded_services;

public class MultiThreadManager {
    private final int numberOfThreads;
    private final Thread[] threads;

    public MultiThreadManager(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.threads = new Thread[numberOfThreads];
        System.out.println("Using " + numberOfThreads + " threads for MultiThreadManager");
    }

    public void execute(ParallelizableWork pw, int nIterations) {
        for (int ti = 0; ti < numberOfThreads; ti++) {
            final int threadId = ti;
            threads[threadId] = new Thread(() -> {

                int start_i = threadId * nIterations / numberOfThreads;
                int end_i = (threadId + 1) * nIterations / numberOfThreads;
                if (threadId == numberOfThreads - 1) {
                    end_i = nIterations;
                }

                pw.doIteration(start_i, end_i);
            });
            threads[threadId].start();
        }

        finish(threads);
    }

    private void finish(Thread[] threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread " + t + " was interrupted");
            }
        }
    }
}
