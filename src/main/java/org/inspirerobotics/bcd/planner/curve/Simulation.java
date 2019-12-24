package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;
import org.inspirerobotics.bcd.planner.ui.Gui;

/**
 * A simulation of a Quadratic Bezier Curve. This simulation
 * will bump time at every step and then move to the next curve
 * when time becomes greater than 1.
 */
public class Simulation {

    private final Gui gui;

    private boolean running;
    private double time;
    private int currentCurve = 1;

    public Simulation(Gui gui) {
        this.gui = gui;

        this.currentCurve = 0;
        this.time = 0.0;
    }

    public void start(){
        running = true;
        time = 0.0;
        currentCurve = 1;
    }

    public void tick(long timerTimeNano){
        if(!running)
            return;

        time += .006;

        if(time > 1.0){
            gotoNextCurve();
        }
    }

    private void gotoNextCurve() {
        currentCurve++;
        time = 0.0;

        if(retrieveCurve() == null){
            running = false;
        }
    }

    public Point2D getCurrentPoint(){
        QBezierCurve curve = retrieveCurve();

        if(curve == null)
            return new Point2D(0, 0);

        double x = calc(curve.getStart().getX(), curve.getControlPoint().getX(), curve.getEnd().getX());
        double y = calc(curve.getStart().getY(), curve.getControlPoint().getY(), curve.getEnd().getY());

        return new Point2D(x, y);
    }

    private double calc(double p0, double p1, double p2){
        double iTime = 1 - time;

        return (Math.pow(iTime, 2) * p0) + (2 * iTime * time * p1)  + (Math.pow(time, 2) * p2);
    }

    private QBezierCurve retrieveCurve() {
        return gui.getCurves().stream().filter(curve -> curve.getName().equals("Curve" + currentCurve)).findAny()
                .orElse(null);
    }

    public boolean isRunning() {
        return running;
    }

    public double getTime() {
        return time;
    }

    public int getCurrentCurve() {
        return currentCurve;
    }

    public Gui getGui() {
        return gui;
    }
}
