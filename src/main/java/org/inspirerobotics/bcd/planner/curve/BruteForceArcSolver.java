package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;

/**
 * An Arc Length Solver that brute forces the arc length by finding the distance
 * of the line iteratively (sum of all of distances between small steps in time)
 */
public class BruteForceArcSolver implements ArcLengthSolver{

    private final double dt;

    public BruteForceArcSolver(double dt) {
        this.dt = dt;
    }

    @Override
    public double solve(QBezierCurve curve) {
        Point2D prev = ArcLengthSolver.calc(curve, 0);
        double result = 0;

        for(double t = dt; t < 1; t += dt){
            Point2D curr = ArcLengthSolver.calc(curve, t);

            result += prev.distance(curr);

            prev = curr;
        }

        return result;
    }

    @Override
    public String getName() {
        return "Brute Force (dt=" + dt + ")";
    }
}
