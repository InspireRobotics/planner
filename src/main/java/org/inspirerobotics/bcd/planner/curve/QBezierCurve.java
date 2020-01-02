package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * Represents a Quadratic Bezier curve where:
 * <p>P<sub>0</sub> = Starting Point</p>
 * <p>P<sub>1</sub> = Control Point</p>
 * <p>P<sub>2</sub> = End Point</p>
 */
public class QBezierCurve {

    private static int count = 0;

    private final String name;
    private Color color = Color.RED;
    private Point2D start = new Point2D(0, 0);
    private Point2D end = new Point2D(0, 0);
    private Point2D controlPoint = new Point2D(0, 0);

    public QBezierCurve() {
        this("Curve" + (++count));
    }

    public QBezierCurve(String name) {
        this.name = name;
    }

    public static void resetCount() {
        count = 0;
    }

    public QBezierCurve copy() {
        QBezierCurve curve = new QBezierCurve(name);

        curve.setStart(copyPoint(start));
        curve.setControlPoint(copyPoint(controlPoint));
        curve.setEnd(copyPoint(end));
        curve.setColor(Color.color(color.getRed(), color.getGreen(), color.getBlue()));

        return curve;
    }

    private Point2D copyPoint(Point2D point) {
        return new Point2D(point.getX(), point.getY());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setControlPoint(Point2D controlPoint) {
        this.controlPoint = controlPoint;
    }

    public void setEnd(Point2D end) {
        this.end = end;
    }

    public void setStart(Point2D start) {
        this.start = start;
    }

    public Point2D getControlPoint() {
        return controlPoint;
    }

    public Point2D getEnd() {
        return end;
    }

    public Point2D getStart() {
        return start;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
