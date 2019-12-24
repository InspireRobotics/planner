package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;

/**
 * Different functions and classes used by the Arc-Length solvers
 */
public interface ArcLengthSolver {

    String getName();

    double solve(QBezierCurve curve);

    class Result{
        public final double time;
        public final double avgResult;

        public Result(double time, double avgResult) {
            this.time = time;
            this.avgResult = avgResult;
        }
    }

    static Point2D calc(QBezierCurve curve, double time){
        double x = calc(time, curve.getStart().getX(), curve.getControlPoint().getX(), curve.getEnd().getX());
        double y = calc(time, curve.getStart().getY(), curve.getControlPoint().getY(), curve.getEnd().getY());

        return new Point2D(x, y);
    }

    static double calc(double time, double p0, double p1, double p2){
        double iTime = 1 - time;

        return (Math.pow(iTime, 2) * p0) + (2 * iTime * time * p1)  + (Math.pow(time, 2) * p2);
    }

}
