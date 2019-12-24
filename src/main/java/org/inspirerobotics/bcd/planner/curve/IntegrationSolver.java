package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;

/**
 * A solver based on an integration from the internet.
 * The code was originally written in C, but was converted into Java.
 *<p></p>
 * Source: <a href="https://malczak.linuxpl.com/blog/quadratic-bezier-curve-length/">Matt's Blog Post</a></a>
 * @author Mateusz Małczak
 */
public class IntegrationSolver implements ArcLengthSolver{

    private QBezierCurve curve;

    @Override
    public double solve(QBezierCurve curve) {
        Point2D p0 = curve.getStart();
        Point2D p1 = curve.getControlPoint();
        Point2D p2 = curve.getEnd();

        Point2D a = new Point2D(
            p0.getX() + ((-2 * p1.getX()) + p2.getX()),
                p0.getY() + ((-2 *p1.getY()) + p2.getY())
        );

        Point2D b = new Point2D(
                (2 * p1.getX()) - (2*p0.getX()),
                (2 * p1.getY()) - (2*p0.getY())
        );

        double A = 4*(a.getX()*a.getX() + a.getY()*a.getY());
        double B = 4*(a.getX()*b.getX() + a.getY()*b.getY());
        double C = b.getX()*b.getX() + b.getY()*b.getY();

        double Sabc = 2*Math.sqrt(A+B+C);
        double A_2 = Math.sqrt(A);
        double A_32 = 2*A*A_2;
        double C_2 = 2*Math.sqrt(C);
        double BA = B/A_2;

        return ( A_32*Sabc +
                A_2*B*(Sabc-C_2) +
                (4*C*A-B*B)*Math.log( (2*A_2+BA+Sabc)/(BA+C_2) )
        )/(4*A_32);
    }

    @Override
    public String getName() {
        return "Integration Solver";
    }
}
