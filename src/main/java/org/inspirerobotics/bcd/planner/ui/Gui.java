package org.inspirerobotics.bcd.planner.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import org.inspirerobotics.bcd.planner.Images;
import org.inspirerobotics.bcd.planner.Launcher;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;
import org.inspirerobotics.bcd.planner.curve.Simulation;

/**
 * Main class for responsible for creating/handling the GUI
 *
 * This is created from the Launcher class after JavaFX is initialized
 */
public class Gui {

    private final Stage stage;
    private final MainScene scene;
    private final Simulation simulation;

    private ObservableList<QBezierCurve> curves;

    public Gui(Stage stage) {
        this.stage = stage;
        this.curves = FXCollections.observableArrayList();
        this.simulation = new Simulation(this);
        this.scene = new MainScene(this);

        initStageSettings(stage);
        addTestCurves();

        this.stage.setScene(scene.getScene());
        this.stage.show();

        GuiUtils.createTimer(simulation::tick).start();
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("BCD Planner: " + Launcher.VERSION);
        stage.setResizable(true);
        stage.getIcons().add(Images.getIcon());

        stage.setWidth(900);
        stage.setHeight(650);
    }

    public void stop() {

    }

    private void addTestCurves() {
        QBezierCurve testCurve = new QBezierCurve();
        testCurve.setStart(new Point2D(25, 100));
        testCurve.setControlPoint(new Point2D(300, 75));
        testCurve.setEnd(new Point2D(300, 400));

        QBezierCurve testCurve2 = new QBezierCurve();
        testCurve2.setStart(new Point2D(300, 400));
        testCurve2.setControlPoint(new Point2D(300, 540));
        testCurve2.setEnd(new Point2D(370, 570));

        curves.add(testCurve);
        curves.add(testCurve2);
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public ObservableList<QBezierCurve> getCurves() {
        return curves;
    }
}
