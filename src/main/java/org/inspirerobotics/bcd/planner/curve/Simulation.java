package org.inspirerobotics.bcd.planner.curve;

import javafx.geometry.Point2D;
import org.inspirerobotics.bcd.planner.ui.Gui;

/**
 * A simulation of a Quadratic Bezier Curve. It tracks the distance
 * the robot has travelled and then finds the time by iterating until the distance is found.
 */
public class Simulation {

    private final Gui gui;

    private boolean running;
    private double distance = 0;
    private int currentCurve = 1;
    private double currentBezierTime;

    private Point2D robotPos;

    private long timeRan;
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
        timeRan = 0;
        currentBezierTime = 0;
        lastTimeMs = System.currentTimeMillis();

        robotPos = getCurrentPoint();
    }

    public void tick(long timerTimeNano){
        if(!running)
            return;

        double deltaPos = getDeltaPosition();
        double angle = getAngle();

        double deltaX = Math.cos(angle) * deltaPos;
        double deltaY = Math.sin(angle) * deltaPos;

        distance += Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        robotPos = robotPos.add(deltaX, deltaY);
        currentBezierTime = calcTimeAtDistance(distance);

        if(getTime() > 1.0){
            gotoNextCurve();
        }

        timeRan += System.currentTimeMillis() - lastTimeMs;
        lastTimeMs = System.currentTimeMillis();
    }

    private double getDeltaPosition() {
        double deltaTime = (System.currentTimeMillis() - lastTimeMs) / 1000.0;
        double robotSpeedFeetPerSec = 5;
        return robotSpeedFeetPerSec * deltaTime;
    }

    private void gotoNextCurve() {
        currentCurve++;
        distance = 0.0;
        lastTimeMs = System.currentTimeMillis();

        if(retrieveCurve() == null){
            running = false;
        }
    }

    public Point2D getCurrentPoint(){
        QBezierCurve curve = retrieveCurve();

        if(curve == null)
            return new Point2D(0, 0);

        double time = getTime();
        double x = calc(curve.getStart().getX(), curve.getControlPoint().getX(), curve.getEnd().getX(), time);
        double y = calc(curve.getStart().getY(), curve.getControlPoint().getY(), curve.getEnd().getY(), time);

        return new Point2D(x, y);
    }

    private double calcTimeAtDistance(double targetDistance){
        QBezierCurve curve = retrieveCurve();

        Point2D prev = ArcLengthSolver.calc(curve, 0.0);
        double currDistance = 0;
        double dt = .001;

        for(double t = 0; t < 1; t += dt){
            Point2D curr = ArcLengthSolver.calc(curve, t);

            currDistance += curr.distance(prev);

            if(currDistance >= targetDistance){
                return t;
            }

            prev = curr;
        }

        return currDistance;
    }

    private double calc(double p0, double p1, double p2, double time){
        double iTime = 1 -  time;

        return (Math.pow(iTime, 2) * p0) + (2 * iTime * time * p1)  + (Math.pow(time, 2) * p2);
    }

    private QBezierCurve retrieveCurve() {
        return gui.getCurves().stream().filter(curve -> curve.getName().equals("Curve" + currentCurve)).findAny()
                .orElse(null);
    }

    private double calcDerivative(double p0, double p1, double p2, double time){
        return (2 * (1 - time) * (p1 - p0)) + (2 * time * (p2 - p1));
    }

    private double calcAngle(QBezierCurve c, double time){
        double velX = calcDerivative(c.getStart().getX(), c.getControlPoint().getX(), c.getEnd().getX(), time);
        double velY = calcDerivative(c.getStart().getY(), c.getControlPoint().getY(), c.getEnd().getY(), time);

        return Math.atan2(velY, velX);
    }

    public double getAngle(){
        if(retrieveCurve() == null)
            return Double.NaN;

        QBezierCurve curve = retrieveCurve();
        double time = getTime();

        return calcAngle(curve, time);
    }

    public double getTime(){
        return currentBezierTime;
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentCurve() {
        return currentCurve;
    }

    public long getTimeRan() {
        return timeRan;
    }

    public Point2D getRobotPos() {
        return robotPos;
    }
}
