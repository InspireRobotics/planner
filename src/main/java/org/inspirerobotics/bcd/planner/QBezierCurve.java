package org.inspirerobotics.bcd.planner;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class QBezierCurve {

    private Color color = Color.RED;
    private Point2D start = new Point2D(0, 0);
    private Point2D end = new Point2D(0, 0);
    private Point2D controlPoint = new Point2D(0, 0);

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
}
