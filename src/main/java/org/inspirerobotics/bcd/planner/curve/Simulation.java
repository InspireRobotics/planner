package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;
import org.inspirerobotics.bcd.planner.ui.Gui;

/**
 * A simulation of a Quadratic Bezier Curve. This simulation
 * will move the robot at a constant speed. It also tracks the distance
 * the robot has travelled and divides that by the total length of the
 * curve. The length of the curve is calculated using the
 * {@link IntegrationSolver} class
 */
public class Simulation {

    private final Gui gui;

    private boolean running;
    private double distance = 0;
    private int currentCurve = 1;

    private long longLongTime;
    private long lastTimeMs;

    public Simulation(Gui gui) {
        this.gui = gui;

        this.currentCurve = 0;
        this.distance = 0.0;
    }

    public void start(){
        running = true;
        distance = 0.0;
        currentCurve = 1;
        lastTimeMs = System.currentTimeMillis();
        longLongTime = System.currentTimeMillis();
    }

    public void tick(long timerTimeNano){
        if(!running)
            return;

        double deltaTime = (System.currentTimeMillis() - lastTimeMs) / 1000.0;
        double robotSpeedFeetPerSec = 5;
        double velocity = robotSpeedFeetPerSec * deltaTime;
        double xVel = Math.sin(getAngle()) * velocity;
        double yVel = Math.cos(getAngle()) * velocity;
        distance += Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));

        if(getTime() > 1.0){
            gotoNextCurve();
        }

        lastTimeMs = System.currentTimeMillis();
    }

    private void gotoNextCurve() {
        currentCurve++;
        distance = 0.0;
        lastTimeMs = System.currentTimeMillis();

        if(retrieveCurve() == null){
            running = false;
            System.out.println(((System.currentTimeMillis() - longLongTime)));
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

    public double getTime(){
        return distance / new IntegrationSolver().solve(retrieveCurve());
    }

    private double calc(double p0, double p1, double p2){
        double iTime = 1 -  getTime();

        return (Math.pow(iTime, 2) * p0) + (2 * iTime * getTime() * p1)  + (Math.pow(getTime(), 2) * p2);
    }

    private QBezierCurve retrieveCurve() {
        return gui.getCurves().stream().filter(curve -> curve.getName().equals("Curve" + currentCurve)).findAny()
                .orElse(null);
    }

    private double calcDerivative(double p0, double p1, double p2){
        return (2 * (1 -  getTime()) * (p1 - p0)) + (2 * getTime() * (p2 - p1));
    }

    public double getAngle(){
        QBezierCurve curve = retrieveCurve();
        double velX = calcDerivative(curve.getStart().getX(), curve.getControlPoint().getX(), curve.getEnd().getX());
        double velY = calcDerivative(curve.getStart().getY(), curve.getControlPoint().getY(), curve.getEnd().getY());

        return Math.atan2(velY, velX);
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentCurve() {
        return currentCurve;
    }

    public Gui getGui() {
        return gui;
    }
}
