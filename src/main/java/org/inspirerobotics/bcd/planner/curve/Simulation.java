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

    private Point2D robotPos;

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

        robotPos = getCurrentPoint();
    }

    public void tick(long timerTimeNano){
        if(!running)
            return;

        double deltaTime = (System.currentTimeMillis() - lastTimeMs) / 1000.0;
        double robotSpeedFeetPerSec = 1;
        double deltaPos = robotSpeedFeetPerSec * deltaTime;
        double angle = getAngle();

        double deltaX = Math.cos(angle) * deltaPos;
        double deltaY = Math.sin(angle) * deltaPos;

        distance += Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        robotPos = robotPos.add(deltaX, deltaY);

        System.out.printf("Drive: speed=%f   theta=%f   d=%f\n", robotSpeedFeetPerSec, angle, distance);

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
        }
        System.out.println("new curve");
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

    private double getTime(double distance){
        return distance / new BruteForceArcSolver(.001).solve(retrieveCurve());
    }

    public double getTime(){
        return getTime(distance);
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
        QBezierCurve curve = retrieveCurve();
        double time = getTime();

        return calcAngle(curve, time);
    }

    public boolean isRunning() {
        return running;
    }

    public int getCurrentCurve() {
        return currentCurve;
    }


    public double getDistance() {
        return distance;
    }

    public Point2D getRobotPos() {
        return robotPos;
    }

    public Gui getGui() {
        return gui;
    }
}
