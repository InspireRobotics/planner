package org.inspirerobotics.bcd.planner.curve;

/**
 * A arc length solver based on the Control Polygon algorithm.
 *<p></p>
 * Source: <a href="https://raphlinus.github.io/curves/2018/12/28/bezier-arclength.html#the-control-polygon-length-approach">
 *     Raph Levien's Blog Post</a>
 * @author Jens Gravesen
 */
public class ControlPolygonSolver implements ArcLengthSolver {

    @Override
    public double solve(QBezierCurve curve) {
        double chordLength = curve.getEnd().distance(curve.getStart());
        double sideOne = curve.getControlPoint().distance(curve.getStart());
        double sideTwo = curve.getEnd().distance(curve.getControlPoint());
        double perimeter = sideOne + sideTwo;

        return ((2 * chordLength) + perimeter) / 3;
    }

    @Override
    public String getName() {
        return "Control Polygon";
    }
}
