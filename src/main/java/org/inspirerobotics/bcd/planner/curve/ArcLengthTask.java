package org.inspirerobotics.bcd.planner.curve;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 * This task is responsible for testing different Arc-Length algorithms. It preforms the task multiple
 * times and then averages all of the results together.
 */
public class ArcLengthTask extends Task<ArcLengthSolver.Result> {

    public static final int COUNT = 100_000;

    private final ProgressBar bar;
    private final ArcLengthSolver solver;
    private final QBezierCurve curve;

    public ArcLengthTask(QBezierCurve curve, ArcLengthSolver solver, ProgressBar bar) {
        this.curve = curve;
        this.solver = solver;
        this.bar = bar;

        this.bar.setProgress(0);
    }

    @Override
    protected ArcLengthSolver.Result call() {
        double result = 0;
        long time = System.currentTimeMillis();

        for(int i = 0; i < COUNT; i++){
            if(this.isCancelled()){
                return null;
            }

            result += solver.solve(curve);

            if(i % 1000 == 0){
                syncProgressBar(i);
            }
        }

        time = System.currentTimeMillis() - time;
        double timePer = (double) time / COUNT;
        result /= COUNT;

        return new ArcLengthSolver.Result(timePer, result);
    }

    private void syncProgressBar(int i) {
        Platform.runLater(() -> bar.setProgress((double) i / COUNT));
    }

    public static Service<ArcLengthSolver.Result> createService(QBezierCurve curve, ArcLengthSolver solver, ProgressBar bar){
        return new Service<>() {
            @Override
            protected ArcLengthTask createTask() {
                return new ArcLengthTask(curve, solver, bar);
            }
        };
    }
}
