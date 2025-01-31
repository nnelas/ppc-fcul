package pt.fcul.ppc.nnelas.nbody;

import java.util.Random;
import java.util.stream.IntStream;

public class NBodySystem {

    public static final int DEFAULT_ITERATIONS = 5;
    public static final int DEFAULT_SIZE = 50000;

    public static final int ADVANCE_THRESHOLD = 1000;
    public static final int APPLY_THRESHOLD = 100;

    static final double PI = 3.141592653589793;
    static final double SOLAR_MASS = 4 * PI * PI;

    protected NBody[] bodies;

    public NBodySystem(int n, long seed) {
        Random random = new Random(seed);
        bodies = new NBody[n];

        double px = 0.0;
        double py = 0.0;
        double pz = 0.0;
        for (int i = 0; i < n; i++) {
            bodies[i] = new NBody(random);
            px += bodies[i].vx * bodies[i].mass;
            py += bodies[i].vy * bodies[i].mass;
            pz += bodies[i].vz * bodies[i].mass;
        }
        bodies[0].offsetMomentum(px, py, pz);
    }

    public void advance(double dt) {
        IntStream.range(0, bodies.length).parallel().forEachOrdered(i -> {
            NBody iBody = bodies[i];
            IntStream.range(i + 1, bodies.length).parallel().forEachOrdered(j -> {
                makeCalculations(iBody, bodies[j], dt);
            });

            moveBody(iBody, dt);
        });
    }

    private void makeCalculations(NBody iBody, NBody body, double dt) {
        double dx = iBody.x - body.x;
        double dy = iBody.y - body.y;
        double dz = iBody.z - body.z;

        double dSquared = dx * dx + dy * dy + dz * dz;
        double distance = Math.sqrt(dSquared);
        double mag = dt / (dSquared * distance);

        iBody.vx -= dx * body.mass * mag;
        iBody.vy -= dy * body.mass * mag;
        iBody.vz -= dz * body.mass * mag;

        body.vx += dx * iBody.mass * mag;
        body.vy += dy * iBody.mass * mag;
        body.vz += dz * iBody.mass * mag;
    }

    private void moveBody(NBody body, double dt) {
        body.x += dt * body.vx;
        body.y += dt * body.vy;
        body.z += dt * body.vz;
    }

    public double energy() {
        double dx, dy, dz, distance;
        double e = 0.0;

        for (int i = 0; i < bodies.length; ++i) {
            NBody iBody = bodies[i];
            e += 0.5 * iBody.mass * (iBody.vx * iBody.vx + iBody.vy * iBody.vy + iBody.vz * iBody.vz);

            for (int j = i + 1; j < bodies.length; ++j) {
                NBody jBody = bodies[j];
                dx = iBody.x - jBody.x;
                dy = iBody.y - jBody.y;
                dz = iBody.z - jBody.z;

                distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                e -= (iBody.mass * jBody.mass) / distance;
            }
        }
        return e;
    }
}