package pt.fcul.ppc.nnelas.nbody;


public class Main {
    /*  SEQUENTIAL RESULTS
        -585978267.836965900
        4236391003.689768300
        4197810927.501643700
        4192253663.992889000
        4190446554.562777000
        4189647285.104568000
     */

    public static void main(String[] args) {

        NBodySystem bodies = new NBodySystem(NBodySystem.DEFAULT_SIZE, 1L);
        System.out.printf("%.9f\n", bodies.energy());
        for (int i = 0; i < NBodySystem.DEFAULT_ITERATIONS; ++i) {
            bodies.advance(0.01);
            System.out.printf("%.9f\n", bodies.energy());
        }
    }
}
